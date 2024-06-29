package com.example.ertebatfardaboarding.domain.dto;

import com.example.ertebatfardaboarding.domain.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String username;
    private Boolean isActive = false;
    private String activationCode;
    private List<Role> roles;
}
