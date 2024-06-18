package com.example.ertebatfardaboarding.security;

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
import java.util.ArrayList;

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
    ResponseModel responseModel;


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            username =null;
//            username =dDosControlService.tokenVerification(request);
//            if (username != null && userDetailsService.loadUserByUsername(username).geti)
//        }catch (){}
//    }


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
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResult(-1);
            responseModel.setSystemError(e.getMessage());
            responseModel.setError(e.getMessage());
            responseModel.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(responseModel));
        } catch (Exception e) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResult(-1);
            responseModel.setSystemError(e.toString());
            responseModel.setError(e.getMessage());
            responseModel.setContent(new Object());
            responseModel.setContents(new ArrayList<>());
            responseModel.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(responseModel));
        }
//        if (username != null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken
//                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
        chain.doFilter(request, response);
    }


}
