package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.Attachment;
import com.example.ertebatfardaboarding.domain.AttachmentDto;
import com.example.ertebatfardaboarding.domain.mapper.AttachmentMapper;
import com.example.ertebatfardaboarding.exception.AttachmentException;
import com.example.ertebatfardaboarding.repo.AttachmentRepository;
import com.example.ertebatfardaboarding.service.FileStorageService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;

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

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    private final Path fileStorageLocation;

    @Value("${file.upload-dir}")
    String uploadDir;

    @Override
    public Page<Attachment> getAttachments(Integer pageNo, Integer perPage) throws Exception {
        return attachmentRepository.findAll(ErtebatFardaBoardingApplication.createPagination(pageNo, perPage));
    }

    @Override
    public Attachment getAttachmentById(Long id) throws Exception {
        return attachmentRepository.findById(id).orElseThrow(() -> new Exception(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH)));
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

    public AttachmentDto storeFile(MultipartFile file, Authentication authentication) throws IOException {
        String username = authentication.getName();

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (fileName.contains("..")) {
            throw new AttachmentException(faMessageSource.getMessage("INVALID_PATH", null, Locale.ENGLISH) + fileName);
        }
        if (file.getSize() >= 5000000) {
            throw new AttachmentException(faMessageSource.getMessage("FILE_SIZE", null, Locale.ENGLISH));
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
        return AttachmentMapper.attachmentMapper.attachmentToAttachmentDto(savedAttachment);
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
    public Attachment getAllUserPhotos(Long photoId, String fileToken, HttpServletResponse httpServletResponse) throws Exception {
        UserDetails userDetails = (UserDetails) redisTemplate.opsForValue().get(fileToken);
        Attachment attachmentById = getAttachmentById(photoId);
        if (userDetails == null || attachmentById == null)
            throw new AttachmentException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH));
        Path targetLocation = this.fileStorageLocation.resolve(attachmentById.getFileName());
        attachmentById.setAccessUrl(targetLocation.toString());
        return attachmentById;
    }

    @Override
    public AttachmentDto getAllUserPhotosAsPhoto(Long photoId, String fileToken, HttpServletResponse httpServletResponse) throws Exception {
        UserDetails userDetails = (UserDetails) redisTemplate.opsForValue().get(fileToken);
        Attachment attachmentById = getAttachmentById(photoId);
        if (userDetails == null || attachmentById == null)
            throw new AttachmentException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH));
        String accessUrl = attachmentById.getAccessUrl().replaceAll("(?<!http:)//", "/");
        AttachmentDto attachmentDto = AttachmentMapper.attachmentMapper.attachmentToAttachmentDto(attachmentById);
        attachmentDto.setFileData(readImageAsByteArray(accessUrl));
        return attachmentDto;
    }

    public static byte[] readImageAsByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] byteArray = FileCopyUtils.copyToByteArray(inputStream);
            return byteArray;
        }
    }

}
