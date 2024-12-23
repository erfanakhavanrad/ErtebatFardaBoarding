package com.example.ertebatfardaboarding.controller;

import com.example.ertebatfardaboarding.domain.ErrorResponseModel;
import com.example.ertebatfardaboarding.exception.BadRequestException;
import com.example.ertebatfardaboarding.exception.ForbiddenException;
import com.example.ertebatfardaboarding.exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisory extends ResponseEntityExceptionHandler {

    ErrorResponseModel responseModel = new ErrorResponseModel();

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponseModel badRequest(BadRequestException badRequest, WebRequest webRequest) {
        responseModel.setError("Exception with message: " + badRequest.getMessage());
        responseModel.setStatus(HttpStatus.BAD_REQUEST.value());
        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
        return responseModel;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    public ErrorResponseModel unauthorized(UnAuthorizedException unauthorized, WebRequest webRequest) {
        responseModel.setError("Exception with message: " + unauthorized.getMessage());
        responseModel.setStatus(HttpStatus.UNAUTHORIZED.value());
        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
        return responseModel;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorResponseModel forbidden(ForbiddenException forbidden, WebRequest webRequest) {
        responseModel.setError("Exception with message: " + forbidden.getMessage());
        responseModel.setStatus(HttpStatus.FORBIDDEN.value());
        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
        return responseModel;
    }

//    @ResponseBody
//    @ExceptionHandler(ContactException.class)
//    public ErrorResponseModel handleContactException(ContactException contactException, WebRequest webRequest) {
//        responseModel.setError("contactException with message: " + contactException.getMessage());
//        responseModel.setStatus(HttpStatus.NOT_FOUND.value());
//        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
//        return responseModel;
//    }
//
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(UserException.class)
//    public ErrorResponseModel handleUserException(UserException userException, WebRequest webRequest) {
//        responseModel.setError("userException with message: " + userException.getMessage());
//        responseModel.setStatus(HttpStatus.CONFLICT.value());
//        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
//        return responseModel;
//    }
//
//    @ResponseBody
//    @ExceptionHandler(AttachmentException.class)
//    public ErrorResponseModel handleAttachmentException(AttachmentException attachmentException, WebRequest webRequest) {
//        responseModel.setError("attachmentException with message: " + attachmentException.getMessage());
//        responseModel.setStatus(HttpStatus.BAD_REQUEST.value());
//        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
//        return responseModel;
//    }
//
//    @ResponseBody
//    @ExceptionHandler(RoleException.class)
//    public ErrorResponseModel handleRoleException(RoleException roleException, WebRequest webRequest) {
//        responseModel.setError("RoleException with message: " + roleException.getMessage());
//        responseModel.setStatus(HttpStatus.BAD_REQUEST.value());
//        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
//        return responseModel;
//    }
//
//    @ResponseBody
//    @ExceptionHandler(PrivilegeException.class)
//    public ErrorResponseModel handlePrivilegeException(PrivilegeException privilegeException, WebRequest webRequest) {
//        responseModel.setError("PrivilegeException with message: " + privilegeException.getMessage());
//        responseModel.setStatus(HttpStatus.BAD_REQUEST.value());
//        responseModel.setTimestamp(String.valueOf(LocalDateTime.now()));
//        return responseModel;
//    }

}
