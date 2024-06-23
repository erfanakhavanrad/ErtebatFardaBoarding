package com.example.ertebatfardaboarding.service;

import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDto registerUser(UserDto userDto, HttpServletRequest httpServletRequest) throws UserException;

    UserDto verifyUser(UserDto userDto, HttpServletRequest httpServletRequest) throws UserException;

    Page<User> getUsers(Integer pageNo, Integer perPage) throws Exception;

    UserDto loginUser(UserDto userDto, HttpServletRequest httpServletRequest);
}
