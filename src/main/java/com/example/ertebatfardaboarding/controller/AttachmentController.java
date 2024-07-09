package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.responseDto.AttachmentResponseDto;
import com.example.ertebatfardaboarding.service.FileStorageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("attachment")
@Slf4j
public class AttachmentController {

    ResponseModel responseModel = new ResponseModel();

    @Autowired
    FileStorageService fileStorageService;

    @PreAuthorize("hasAuthority('ATTACHMENT,READ')")
    @GetMapping("/getAll")
    public ResponseModel getAll(@RequestParam Integer pageNo, Integer perPage, HttpServletResponse httpServletResponse) throws Exception {
        responseModel.clear();
        log.info("get all attachments");
        Page<AttachmentResponseDto> attachments = fileStorageService.getAttachments(pageNo, perPage);
        responseModel.setContents(attachments.getContent());
        responseModel.setRecordCount((int) attachments.getTotalElements());
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('ATTACHMENT,READ')")
    @GetMapping("/getById")
    public ResponseModel getById(@RequestParam Long id, HttpServletResponse httpServletResponse) throws Exception {
        log.info("get attachment by id");
        responseModel.clear();
        responseModel.setContent(fileStorageService.getAttachmentById(id));
        responseModel.setRecordCount(1);
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('ATTACHMENT,CREATE')")
    @PostMapping("/uploadPhoto")
    public ResponseModel uploadPhoto(@RequestParam("File") MultipartFile file, Authentication authentication) throws IOException {
        responseModel.clear();
        AttachmentResponseDto attachmentDto = fileStorageService.storeFile(file, authentication);
        responseModel.setContent(attachmentDto);
        return responseModel;
    }

    @PreAuthorize("hasAuthority('ATTACHMENT,READ')")
    @GetMapping("/allUserPhotos")
    public ResponseModel getAllUserPhotos(@RequestParam Long photoId, @RequestParam String fileToken, HttpServletResponse httpServletResponse) throws Exception {
        responseModel.clear();
        AttachmentResponseDto file = fileStorageService.getAllUserPhotos(photoId, fileToken, httpServletResponse);
        responseModel.setContent(file);
        responseModel.setRecordCount(1);
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('ATTACHMENT,READ')")
    @GetMapping("/getAllUserPhotosAsPhoto")
    public ResponseEntity<org.springframework.core.io.Resource> getAllUserPhotosAsPhoto(@RequestParam Long photoId, @RequestParam String fileToken, HttpServletResponse httpServletResponse) throws Exception {
        responseModel.clear();
        AttachmentResponseDto file = fileStorageService.getAllUserPhotosAsPhoto(photoId, fileToken, httpServletResponse);
        responseModel.setContent(file);
        responseModel.setRecordCount(1);
        responseModel.setStatus(httpServletResponse.getStatus());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName()
                        + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

    @PreAuthorize("hasAuthority('ATTACHMENT,DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseModel delete(@PathVariable("id") Long id, HttpServletResponse httpServletResponse) throws Exception {
        log.info("delete attachment");
        responseModel.clear();
        fileStorageService.deletePhoto(id);
        responseModel.setStatus(httpServletResponse.getStatus());
        responseModel.clear();
        return responseModel;
    }

}
