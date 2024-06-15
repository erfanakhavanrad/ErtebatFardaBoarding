package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.domain.dto.UserDto;
import com.example.ertebatfardaboarding.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

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

    @PostMapping(path = "/register")
    public ResponseModel register(@RequestBody UserDto userDto, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException {
        log.info("registering user with ip: {}", ErtebatFardaBoardingApplication.getClientIP(httpServletRequest));
        responseModel.clear();
        responseModel.setContent(userService.registerUser(userDto, httpServletRequest));
        return responseModel;
    }


}
