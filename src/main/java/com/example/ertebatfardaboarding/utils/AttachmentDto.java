package com.example.ertebatfardaboarding.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class AttachmentDto {
    private Long id;
    private String fileName;
    private String fileType;
    private String accessUrl;
    @JsonIgnore
    private long fileSize;
}
