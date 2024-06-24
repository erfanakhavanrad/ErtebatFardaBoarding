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
import org.springframework.data.domain.Page;
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
    public ResponseModel uploadPhoto(@RequestParam("File") MultipartFile file) throws IOException {
        responseModel.clear();
        AttachmentDto attachmentDto = fileStorageService.storeFile(file);
        responseModel.setContent(attachmentDto);
        return responseModel;
    }

    @GetMapping("/downloadPhoto/{photoId}")
    public ResponseModel downloadPhoto(@PathVariable String photoId) throws Exception {
        responseModel.clear();
//        String s = fileStorageService.storeFile2(file);
        Attachment file = fileStorageService.getFile(photoId);
        responseModel.setContent(file);
//        responseModel.setContent(s);
        return responseModel;
    }


}
