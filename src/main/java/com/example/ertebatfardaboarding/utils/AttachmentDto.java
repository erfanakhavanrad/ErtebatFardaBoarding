package com.example.ertebatfardaboarding.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class AttachmentDto {
    private Long id;
    private String fileName;
    private String fileType;
    private String accessUrl;
    private String username;
    @JsonIgnore
    private long fileSize;
    @Lob
    private byte[] fileData;
}
