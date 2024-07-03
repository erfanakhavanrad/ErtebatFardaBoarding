package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.User;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    ResponseModel responseModel;

    @Autowired
    UserService userService;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    @Value("${SUCCESS_RESULT}")
    int success;

    @Value("${FAIL_RESULT}")
    int fail;

    @PreAuthorize("hasAuthority('USER,READ')")
    @GetMapping("/getAll")
    public ResponseModel getAll(@RequestParam Integer pageNo, Integer perPage, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            responseModel.clear();
            log.info("get all users");
            Page<User> users = userService.getUsers(pageNo, perPage);
            responseModel.setContents(users.getContent());
            responseModel.setResult(success);
            responseModel.setRecordCount((int) users.getTotalElements());
            responseModel.setStatus(httpServletResponse.getStatus());
        } catch (AccessDeniedException accessDeniedException) {
            responseModel.setError(faMessageSource.getMessage("ACCESS_DENIED", null, Locale.ENGLISH));
            responseModel.setResult(fail);
            responseModel.setSystemError(accessDeniedException.getMessage());
            responseModel.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            responseModel.setError(e.getMessage());
            responseModel.setResult(fail);
            responseModel.setStatus(httpServletResponse.getStatus());
        }
        return responseModel;
    }

    @GetMapping("/searchUser")
    public ResponseModel searchUser(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            responseModel.clear();
            List<User> users = userService.getUsersBySearch(userDto);
            responseModel.setContents(users);
            responseModel.setResult(success);
            responseModel.setRecordCount((int) users.size());
            responseModel.setStatus(httpServletResponse.getStatus());
        } catch (Exception e) {
            responseModel.setError(e.getMessage());
            responseModel.setResult(fail);
            responseModel.setStatus(httpServletResponse.getStatus());
        }
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

    @DeleteMapping("/delete/{id}")
    public ResponseModel delete(@PathVariable("id") Long id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            log.info("delete user");
            responseModel.clear();
            userService.deleteUser(id);
            responseModel.setStatus(httpServletResponse.getStatus());
            responseModel.clear();
            responseModel.setResult(success);
        } catch (org.springframework.security.access.AccessDeniedException accessDeniedException) {
            responseModel.setError(faMessageSource.getMessage("ACCESS_DENIED", null, Locale.ENGLISH));
            responseModel.setResult(fail);
            responseModel.setSystemError(accessDeniedException.getMessage());
            responseModel.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            responseModel.setStatus(httpServletResponse.getStatus());
            responseModel.setResult(fail);
            responseModel.setError(e.getMessage());
        } finally {
            responseModel.setStatus(httpServletResponse.getStatus());
            responseModel.setResult(fail);
        }
        return responseModel;
    }


}
