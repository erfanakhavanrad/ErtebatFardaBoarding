package com.example.ertebatfardaboarding.domain.responseDto;

import com.example.ertebatfardaboarding.domain.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String username;
    private Boolean isActive = false;
    private String activationCode;
    private List<RoleResponseDto> roles;
}
