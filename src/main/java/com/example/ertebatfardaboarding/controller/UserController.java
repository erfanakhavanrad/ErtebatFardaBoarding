package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.domain.responseDto.UserResponseDto;
import com.example.ertebatfardaboarding.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    ResponseModel responseModel = new ResponseModel();

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('USER,READ')")
    @GetMapping("/getAll")
    public ResponseModel getAll(@RequestParam Integer pageNo, Integer perPage, HttpServletResponse httpServletResponse) throws Exception {
        responseModel.clear();
        log.info("get all users");
        Page<UserResponseDto> users = userService.getUsers(pageNo, perPage);
        responseModel.setContents(users.getContent());
        responseModel.setRecordCount((int) users.getTotalElements());
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PreAuthorize("hasAuthority('USER,READ')")
    @GetMapping("/searchUser")
    public ResponseModel searchUser(@RequestBody UserDto userDto, HttpServletResponse httpServletResponse) {
        responseModel.clear();
        List<UserResponseDto> users = userService.getUsersBySearch(userDto);
        responseModel.setContents(users);
        responseModel.setRecordCount((int) users.size());
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }

    @PostMapping(path = "/register")
    public ResponseModel register(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException {
        log.info("registering user with ip: {}", ErtebatFardaBoardingApplication.getClientIP(httpServletRequest));
        responseModel.clear();
        responseModel.setContent(userService.registerUser(userDto, httpServletRequest));
        return responseModel;
    }

    @PostMapping(path = "/verify")
    public ResponseModel verify(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest) throws Exception {
        log.info("verifying user with ip: {}", ErtebatFardaBoardingApplication.getClientIP(httpServletRequest));
        responseModel.clear();
        responseModel.setContent(userService.verifyUser(userDto, httpServletRequest));
        return responseModel;
    }

    @PostMapping(path = "/login")
    public ResponseModel login(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException {
        log.info("login in user with ip: {}", ErtebatFardaBoardingApplication.getClientIP(httpServletRequest));
        responseModel.clear();
        responseModel.setContent(userService.loginUser(userDto, httpServletRequest));
        return responseModel;
    }

    @PreAuthorize("hasAuthority('USER,DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseModel delete(@PathVariable("id") Long id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        log.info("delete user");
        responseModel.clear();
        userService.deleteUser(id);
        responseModel.setStatus(httpServletResponse.getStatus());
        return responseModel;
    }


}
