package com.example.ertebatfardaboarding.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        http.csrf(httpSecurityCsrfConfigurer -> {
            httpSecurityCsrfConfigurer.disable();
        });
//        http.csrf().disable();

        http.httpBasic(Customizer.withDefaults());


//        httpSecurity.csrf().disable().authorizeRequests();

        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorizeReq -> authorizeReq.requestMatchers(HttpMethod.GET, "/contact/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/user/getAll").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
//                .requestMatchers(HttpMethod.POST, "/*").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                .permitAll()
                .anyRequest().permitAll());

//        httpSecurity.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
//        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(cu)}
//    })
//        httpSecurity.httpBasic(Customizer.withDefaults());
//        httpSecurity.cors(Customizer.withDefaults());
//        httpSecurity.csrf().disable();
//        httpSecurity.cors();
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "UPDATE", "OPTIONS", "*");
            }
        };
    }




//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        final CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(Arrays.asList("https://localhost:8888", "https://www.appsdeveloperblog.com", "*"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE", "OPTIONS"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/users/login", configuration);
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }



}
