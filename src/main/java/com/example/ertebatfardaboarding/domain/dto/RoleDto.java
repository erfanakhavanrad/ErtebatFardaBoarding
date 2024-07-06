package com.example.ertebatfardaboarding.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private Long id;
    private String name;
    private List<PrivilegeDto> privileges;
}
