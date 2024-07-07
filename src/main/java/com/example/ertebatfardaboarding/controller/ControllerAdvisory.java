package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.domain.ErrorResponseModel;
import com.example.ertebatfardaboarding.exception.AttachmentException;
import com.example.ertebatfardaboarding.exception.ContactException;
import com.example.ertebatfardaboarding.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisory extends ResponseEntityExceptionHandler {

    @Autowired
    ErrorResponseModel responseModel;

    @ResponseBody
    @ExceptionHandler(ContactException.class)
    public ErrorResponseModel handleContactException(ContactException contactException, WebRequest webRequest) {
        responseModel.setError("contactException with message: " + contactException.getMessage());
        responseModel.setStatus(HttpStatus.NOT_FOUND.value());
        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
        return responseModel;
    }

    @ResponseBody
    @ExceptionHandler(UserException.class)
    public ErrorResponseModel handleUserException(UserException userException, WebRequest webRequest) {
        responseModel.setError("contactException with message: " + userException.getMessage());
        responseModel.setStatus(HttpStatus.CONFLICT.value());
        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
        return responseModel;
    }

    @ResponseBody
    @ExceptionHandler(AttachmentException.class)
    public ErrorResponseModel handleAttachmentException(AttachmentException attachmentException, WebRequest webRequest) {
        responseModel.setError("attachmentException with message: " + attachmentException.getMessage());
        responseModel.setStatus(HttpStatus.BAD_REQUEST.value());
        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
        return responseModel;
    }

}
