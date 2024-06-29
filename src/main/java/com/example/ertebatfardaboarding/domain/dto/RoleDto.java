package com.example.ertebatfardaboarding.domain.dto;

import com.example.ertebatfardaboarding.domain.Privilege;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private Long id;
    private String name;
    private List<Privilege> privileges;
}
