package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.domain.responseDto.LoginResponseDto;
import com.example.ertebatfardaboarding.domain.responseDto.UserResponseDto;
import com.example.ertebatfardaboarding.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto, HttpServletRequest httpServletRequest) throws UserException;

    UserResponseDto verifyUser(UserDto userDto, HttpServletRequest httpServletRequest) throws Exception;

    Page<UserResponseDto> getUsers(Integer pageNo, Integer perPage) throws Exception;

    List<UserResponseDto> getUsersBySearch(UserDto userDto);

    LoginResponseDto loginUser(UserDto userDto, HttpServletRequest httpServletRequest);

    void deleteUser(Long id) throws Exception;
}
