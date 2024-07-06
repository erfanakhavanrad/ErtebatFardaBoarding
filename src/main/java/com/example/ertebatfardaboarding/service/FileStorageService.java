package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.Attachment;
import com.example.ertebatfardaboarding.domain.dto.AttachmentDto;
import com.example.ertebatfardaboarding.domain.responseDto.AttachmentResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    AttachmentResponseDto storeFile(MultipartFile file, Authentication authentication) throws IOException;

    Page<AttachmentResponseDto> getAttachments(Integer pageNo, Integer perPage) throws Exception;

    AttachmentResponseDto getAttachmentById(Long id) throws Exception;

    Path loadFileAsResource(String fileName);

    AttachmentResponseDto getAllUserPhotos(Long photoId, String fileToken, HttpServletResponse httpServletResponse) throws Exception;

    AttachmentResponseDto getAllUserPhotosAsPhoto(Long photoId, String fileToken, HttpServletResponse httpServletResponse) throws Exception;

    void deletePhoto(Long id) throws Exception;
}
