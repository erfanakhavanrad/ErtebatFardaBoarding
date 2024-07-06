package com.example.ertebatfardaboarding.domain.responseDto;

import lombok.Data;

@Data
public class PrivilegeResponseDto {
    private Long id;
    private String name;
    private String domain;
    private String access;
}
