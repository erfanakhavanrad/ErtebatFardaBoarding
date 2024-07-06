package com.example.ertebatfardaboarding.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String username;
    private Boolean isActive = false;
    private String activationCode;
    private List<RoleDto> roles;
}
