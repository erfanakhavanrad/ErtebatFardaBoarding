package com.example.ertebatfardaboarding;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Locale;

@SpringBootApplication
public class ErtebatFardaBoardingApplication {

    final static int PER_PAGE = 20;
    final static int MAX_PAGE_SIZE = 10000;

    public static void main(String[] args) {
        SpringApplication.run(ErtebatFardaBoardingApplication.class, args);
    }

    @Bean
    public MessageSource faMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("message_fa");
        messageSource.setDefaultLocale(Locale.ENGLISH);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public static Pageable createPagination(Integer pageNumber, Integer perPage) {
        if (pageNumber == null)
            return PageRequest.of(0, MAX_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
        else if (perPage == null)
            return PageRequest.of(--pageNumber, PER_PAGE, Sort.by(Sort.Direction.DESC, "id"));
        else
            return PageRequest.of(--pageNumber, perPage, Sort.by(Sort.Direction.DESC, "id"));
    }

    public static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
