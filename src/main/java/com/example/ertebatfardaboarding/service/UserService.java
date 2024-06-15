package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    UserDto registerUser(UserDto userDto, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException;

}
