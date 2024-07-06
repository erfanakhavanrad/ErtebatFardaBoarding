package com.example.ertebatfardaboarding.domain.responseDto;

import com.example.ertebatfardaboarding.domain.Privilege;
import lombok.Data;

import java.util.List;
@Data
public class RoleResponseDto {
    private Long id;
    private String name;
    private List<PrivilegeResponseDto> privileges;
}
