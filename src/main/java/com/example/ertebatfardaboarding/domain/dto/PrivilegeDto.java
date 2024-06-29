package com.example.ertebatfardaboarding.domain.dto;

import lombok.Data;

@Data
public class PrivilegeDto {
    private Long id;
    private String name;
    private String domain;
    private String access;
}
