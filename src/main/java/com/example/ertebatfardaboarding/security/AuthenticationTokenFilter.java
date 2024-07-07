package com.example.ertebatfardaboarding.security;

import com.example.ertebatfardaboarding.domain.ErrorResponseModel;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.service.impl.UserDetailsServiceImpl;
import com.example.ertebatfardaboarding.service.impl.UserServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    public static String username;

    DDosControlService dDosControlService = new DDosControlService();

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    ErrorResponseModel responseModel;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {


            final String authorizationHeader = request.getHeader("Authorization");

            String username = null;
            String jwt = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = dDosControlService.tokenVerification(request);
            }
        } catch (AuthorizationServiceException e) {
            responseModel.setError(e.getMessage());
            responseModel.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(responseModel));
        } catch (Exception e) {
            responseModel.setError(e.getMessage());
            responseModel.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(responseModel));
        }
        chain.doFilter(request, response);
    }


}
