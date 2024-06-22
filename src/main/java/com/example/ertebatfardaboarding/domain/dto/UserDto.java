package com.example.ertebatfardaboarding.domain.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String username;
    private Boolean isActive;
    private String activationCode;
}
