package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.exception.AttachmentException;
import com.example.ertebatfardaboarding.exception.ContactException;
import com.example.ertebatfardaboarding.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    ResponseModel responseModel;

    @Value("${SUCCESS_RESULT}")
    int success;

    @Value("${FAIL_RESULT}")
    int fail;

    @ResponseBody
    @ExceptionHandler(ContactException.class)
    public ResponseModel handleContactException(ContactException contactException, WebRequest webRequest) {
        responseModel.setError("contactException with message: " + contactException.getMessage());
        responseModel.setResult(fail);
        responseModel.setStatus(HttpStatus.NOT_FOUND.value());
        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
        return responseModel;
    }


    @ResponseBody
    @ExceptionHandler(UserException.class)
    public ResponseModel handleUserException(UserException userException, WebRequest webRequest) {
        responseModel.setError("contactException with message: " + userException.getMessage());
        responseModel.setResult(fail);
        responseModel.setStatus(HttpStatus.CONFLICT.value());
        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
        return responseModel;
    }

    @ResponseBody
    @ExceptionHandler(AttachmentException.class)
    public ResponseModel handleAttachmentException(AttachmentException attachmentException, WebRequest webRequest) {
        responseModel.setError("attachmentException with message: " + attachmentException.getMessage());
        responseModel.setResult(fail);
        responseModel.setStatus(HttpStatus.BAD_REQUEST.value());
        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
        return responseModel;
    }

}
