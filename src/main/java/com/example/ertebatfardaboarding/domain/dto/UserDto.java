package com.example.ertebatfardaboarding.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String username;
    private Boolean isActive = false;
    private String activationCode;
}
