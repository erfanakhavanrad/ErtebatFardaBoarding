package com.example.ertebatfardaboarding.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ertebatfardaboarding.ErtebatFardaBoardingApplication;
import com.example.ertebatfardaboarding.utils.GlobalConstants;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
@Component
public class DDosControlService {
    private LoadingCache<String, Integer> attemptCache;

    public DDosControlService() {
        attemptCache = CacheBuilder.newBuilder().build(new CacheLoader<String, Integer>() {
            public Integer load(String key) throws Exception {
                return 0;
            }
        });
    }

    private int numberOfAttempts(HttpServletRequest request) {
        String ipAddress = ErtebatFardaBoardingApplication.getClientIP(request);
        try {
            int count = attemptCache.get(ipAddress);
            attemptCache.put(ipAddress, count + 1);
            return count;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String tokenVerification(HttpServletRequest request) {
        if (request.getHeader(AUTHORIZATION) != null && !request.getServletPath().equals("/user/refresh")) {
            String authHeader = request.getHeader(AUTHORIZATION);
            log.info("path is token: " + request.getServletPath());
            if (authHeader != null && authHeader.startsWith("Bearer") && authHeader.length() > 8) {
                try {
                    String token = authHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(GlobalConstants.SECRET_KEY.getBytes());
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(token);

                    String username = decodedJWT.getSubject();
                    String password = decodedJWT.getAudience().get(0);
                    String[] roles = decodedJWT.getClaim(GlobalConstants.CLAIM_NAME).asArray(String.class);

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("token username: " + username);
                    return username;
                } catch (Exception e) {
                    log.error("Token authentication exception:  " + e.toString());
                }
            }
        }
        return null;
    }

}
