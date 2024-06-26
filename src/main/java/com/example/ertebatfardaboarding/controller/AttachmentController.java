package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.service.FileStorageService;
import com.example.ertebatfardaboarding.utils.Attachment;
import com.example.ertebatfardaboarding.utils.AttachmentDto;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Locale;

@RestController
@RequestMapping("attachment")
@Slf4j
public class AttachmentController {

    @Autowired
    ResponseModel responseModel;

    @Autowired
    FileStorageService fileStorageService;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    @Value("${SUCCESS_RESULT}")
    int success;

    @Value("${FAIL_RESULT}")
    int fail;

    @GetMapping("/getAll")
    public ResponseModel getAll(@RequestParam Integer pageNo, Integer perPage, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            responseModel.clear();
            log.info("get all attachments");
            Page<Attachment> attachments = fileStorageService.getAttachments(pageNo, perPage);
            responseModel.setContents(attachments.getContent());
            responseModel.setResult(success);
            responseModel.setRecordCount((int) attachments.getTotalElements());
            responseModel.setStatus(httpServletResponse.getStatus());
        } catch (AccessDeniedException accessDeniedException) {
            responseModel.setError(faMessageSource.getMessage("ACCESS_DENIED", null, Locale.ENGLISH));
            responseModel.setResult(fail);
            responseModel.setSystemError(accessDeniedException.getMessage());
            responseModel.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            responseModel.setError(e.getMessage());
            responseModel.setResult(fail);
            responseModel.setStatus(httpServletResponse.getStatus());
        }
        return responseModel;
    }

    @GetMapping("/getById")
    public ResponseModel getById(@RequestParam Long id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            log.info("get attachment by id");
            responseModel.clear();
            responseModel.setContent(fileStorageService.getAttachmentById(id));
            responseModel.setRecordCount(1);
            responseModel.setResult(success);
            responseModel.setStatus(httpServletResponse.getStatus());
        } catch (AccessDeniedException accessDeniedException) {
            responseModel.setError(faMessageSource.getMessage("ACCESS_DENIED", null, Locale.ENGLISH));
            responseModel.setResult(fail);
            responseModel.setSystemError(accessDeniedException.getMessage());
            responseModel.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            responseModel.setStatus(httpServletResponse.getStatus());
            responseModel.setResult(fail);
            responseModel.setError(e.getMessage());
        }
        return responseModel;
    }


    @PostMapping("/uploadPhoto")
    public ResponseModel uploadPhoto(@RequestParam("File") MultipartFile file, Authentication authentication) throws IOException {
        responseModel.clear();
        AttachmentDto attachmentDto = fileStorageService.storeFile(file, authentication);
        responseModel.setContent(attachmentDto);
        return responseModel;
    }

    @GetMapping("/allUserPhotos")
    public ResponseModel getAllUserPhotos(@RequestParam Long photoId, @RequestParam String fileToken, HttpServletResponse httpServletResponse) throws Exception {
        responseModel.clear();
        Attachment file = fileStorageService.getAllUserPhotos(photoId, fileToken, httpServletResponse);
        responseModel.setContent(file);
        responseModel.setRecordCount(1);
        responseModel.setResult(success);
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @GetMapping("/getAllUserPhotosAsPhoto")
    public ResponseEntity<org.springframework.core.io.Resource> getAllUserPhotosAsPhoto(@RequestParam Long photoId, @RequestParam String fileToken, HttpServletResponse httpServletResponse) throws Exception {
        responseModel.clear();
        AttachmentDto file = fileStorageService.getAllUserPhotosAsPhoto(photoId, fileToken, httpServletResponse);
        responseModel.setContent(file);
        responseModel.setRecordCount(1);
        responseModel.setResult(success);
        responseModel.setStatus(httpServletResponse.getStatus());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName()
                        + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

}
