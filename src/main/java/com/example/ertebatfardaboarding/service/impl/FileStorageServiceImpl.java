package com.example.ertebatfardaboarding.service.impl;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.mapper.AttachmentMapper;
import com.example.ertebatfardaboarding.exception.AttachmentException;
import com.example.ertebatfardaboarding.repo.AttachmentRepository;
import com.example.ertebatfardaboarding.service.FileStorageService;
import com.example.ertebatfardaboarding.utils.Attachment;
import com.example.ertebatfardaboarding.utils.AttachmentDto;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    private final Path fileStorageLocation;

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

    public AttachmentDto storeFile(MultipartFile file) throws IOException {
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
        attachment.setAccessUrl(targetLocation.toString());

        Attachment savedAttachment = attachmentRepository.save(attachment);
        return AttachmentMapper.attachmentMapper.attachmentToAttachmentDto(savedAttachment);
    }

    @Override
    public Attachment getFile(String fileId) throws Exception {
        return attachmentRepository.findById(Long.valueOf(fileId)).orElseThrow(() -> new Exception(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH)));
    }

    public String storeFile2(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH) + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }


    public Path loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return filePath;
        } catch (Exception ex) {
            throw new RuntimeException(faMessageSource.getMessage("NOT_FOUND", null, Locale.ENGLISH) + fileName, ex);
        }
    }

}
