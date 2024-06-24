package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.Contact;
import com.example.ertebatfardaboarding.utils.Attachment;
import com.example.ertebatfardaboarding.utils.AttachmentDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    AttachmentDto storeFile(MultipartFile file) throws IOException;

    Page<Attachment> getAttachments(Integer pageNo, Integer perPage) throws Exception;

    Attachment getAttachmentById(Long id) throws Exception;

    Attachment getFile(String fileId) throws Exception;

    String storeFile2(MultipartFile file);

    Path loadFileAsResource(String fileName);
}
