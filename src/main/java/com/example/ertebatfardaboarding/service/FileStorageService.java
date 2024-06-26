package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.utils.Attachment;
import com.example.ertebatfardaboarding.utils.AttachmentDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    AttachmentDto storeFile(MultipartFile file, Authentication authentication) throws IOException;

    Page<Attachment> getAttachments(Integer pageNo, Integer perPage) throws Exception;

    Attachment getAttachmentById(Long id) throws Exception;

    Path loadFileAsResource(String fileName);

    Attachment getAllUserPhotos(Long photoId, String fileToken, HttpServletResponse httpServletResponse) throws Exception;

    AttachmentDto getAllUserPhotosAsPhoto(Long photoId, String fileToken, HttpServletResponse httpServletResponse) throws Exception;
}
