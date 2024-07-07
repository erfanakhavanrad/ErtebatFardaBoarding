package com.example.ertebatfardaboarding.domain.responseDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class LoginResponseDto {
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String username;
    private Boolean isActive = false;
    private String activationCode;
    private List<RoleResponseDto> roles;
    private List<ArrayList> tokens;
}
