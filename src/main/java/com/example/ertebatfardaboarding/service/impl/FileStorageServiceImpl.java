package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.Attachment;
import com.example.ertebatfardaboarding.domain.dto.AttachmentDto;
import com.example.ertebatfardaboarding.domain.mapper.AttachmentMapper;
import com.example.ertebatfardaboarding.domain.responseDto.AttachmentResponseDto;
import com.example.ertebatfardaboarding.exception.AttachmentException;
import com.example.ertebatfardaboarding.exception.ForbiddenException;
import com.example.ertebatfardaboarding.repo.AttachmentRepository;
import com.example.ertebatfardaboarding.service.FileStorageService;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Objects;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    AttachmentMapper attachmentMapper;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    private final Path fileStorageLocation;

    @Value("${file.upload-dir}")
    String uploadDir;

    @Override
    public Page<AttachmentResponseDto> getAttachments(Integer pageNo, Integer perPage) throws Exception {
        Page<Attachment> all = attachmentRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
        return all.map(attachmentMapper::attachmentToAttachmentResponseDto);
    }

    @Override
    public AttachmentResponseDto getAttachmentById(Long id) throws Exception {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new AttachmentException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH)));
        return attachmentMapper.attachmentToAttachmentResponseDto(attachment);
    }

    private Attachment getAttachmentByIdDto(Long id) throws Exception {
        return attachmentRepository.findById(id).orElseThrow(() -> new AttachmentException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH)));
    }

    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(faMessageSource.getMessage("DIRECTORY_ERROR", null, Locale.ENGLISH), ex);
        }
    }

    public AttachmentResponseDto storeFile(MultipartFile file, Authentication authentication) throws IOException {
        String username = authentication.getName();

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (fileName.contains("..")) {
            throw new BadRequestException(faMessageSource.getMessage("INVALID_PATH", null, Locale.ENGLISH) + fileName);
        }
        if (file.getSize() >= 5000000) {
            throw new MaxUploadSizeExceededException(5000000);
        }

        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


        Attachment attachment = new Attachment();
        attachment.setFileName(fileName);
        attachment.setFileType(file.getContentType());
        attachment.setAccessUrl(uploadDir + "/" + fileName);
        attachment.setUsername(username);

        Attachment savedAttachment = attachmentRepository.save(attachment);
        attachment.setAccessUrl(targetLocation.toString());
        AttachmentDto attachmentDto = AttachmentMapper.attachmentMapper.attachmentToAttachmentDto(savedAttachment);
        return attachmentMapper.attachmentDtoToAttachmentResponseDto(attachmentDto);
    }

    public Attachment getAttachmentByToken(String token) {
        return attachmentRepository.findByToken(token);
    }

    public Page<Attachment> getAttachmentByUserName(String username, Integer pageNo, Integer perPage) {
        return attachmentRepository.findByUsername(username, ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
    }

    public Path loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return filePath;
        } catch (Exception ex) {
            throw new RuntimeException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH) + fileName, ex);
        }
    }

    @Override
    public AttachmentResponseDto getAllUserPhotos(Long photoId, String fileToken, HttpServletResponse httpServletResponse) throws Exception {
        UserDetails userDetails = (UserDetails) redisTemplate.opsForValue().get(fileToken);
        Attachment attachmentById = getAttachmentByIdDto(photoId);
        if (userDetails == null)
            throw new ForbiddenException(faMessageSource.getMessage("ACCESS_DENIED", null, Locale.ENGLISH));
        if (attachmentById == null)
            throw new EntityNotFoundException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH));
        Path targetLocation = this.fileStorageLocation.resolve(attachmentById.getFileName());
        attachmentById.setAccessUrl(targetLocation.toString());
        return attachmentMapper.attachmentToAttachmentResponseDto(attachmentById);
    }

    @Override
    public AttachmentResponseDto getAllUserPhotosAsPhoto(Long photoId, String fileToken, HttpServletResponse httpServletResponse) throws Exception {
        UserDetails userDetails = (UserDetails) redisTemplate.opsForValue().get(fileToken);
        Attachment attachmentById = getAttachmentByIdDto(photoId);
        if (userDetails == null || attachmentById == null)
            throw new EntityNotFoundException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH));
        String accessUrl = attachmentById.getAccessUrl().replaceAll("(?<!http:)//", "/");
        AttachmentDto attachmentDto = AttachmentMapper.attachmentMapper.attachmentToAttachmentDto(attachmentById);
        attachmentDto.setFileData(readImageAsByteArray(accessUrl));
        return attachmentMapper.attachmentDtoToAttachmentResponseDto(attachmentDto);
    }

    public static byte[] readImageAsByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] byteArray = FileCopyUtils.copyToByteArray(inputStream);
            return byteArray;
        }
    }

    @Override
    public void deletePhoto(Long id) throws Exception {
        attachmentRepository.deleteById(id);
    }

}
