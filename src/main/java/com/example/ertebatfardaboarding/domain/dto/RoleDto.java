package com.example.ertebatfardaboarding.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleDto implements Serializable {
    private Long id;
    private String name;
    private List<PrivilegeDto> privileges;
}
