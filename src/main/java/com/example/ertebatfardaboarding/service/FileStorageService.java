package com.example.ertebatfardaboarding.service;

import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String storeFile(MultipartFile file) throws IOException;
}
