//package com.example.ertebatfardaboarding.utils;
//
//import jakarta.servlet.annotation.WebFilter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@WebFilter("/*")
//public class LoggingFilter implements Filter {
//    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
//
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // Initialization code, if needed
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//
//        long startTime = System.currentTimeMillis();
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//
//        long endTime = System.currentTimeMillis();
//        long elapsedTime = endTime - startTime;
//        long duration = System.currentTimeMillis() - startTime;
//
//        logger.info("Request: {}, Method: {}, Response Status: {}, Duration: {} ms",
//                httpServletRequest.getRequestURI(), httpServletRequest.getMethod(), httpServletResponse.toString(), duration);
//
//    }
//
//    @Override
//    public void destroy() {
//        // Cleanup code, if needed
//    }
//}
