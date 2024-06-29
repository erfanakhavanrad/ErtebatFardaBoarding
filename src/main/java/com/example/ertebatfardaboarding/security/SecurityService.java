package com.example.ertebatfardaboarding.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.ertebatfardaboarding.domain.ResponseModel;
import com.example.ertebatfardaboarding.utils.GlobalConstants;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SecurityService {

    @Autowired
    ResponseModel responseModel;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${SUCCESS_RESULT}")
    int success;

    @Value("${FAIL_RESULT}")
    int fail;

    @Resource(name = "faMessageSource")
    private MessageSource faMessageSource;

    public SecurityModel createTokenByUserPasswordAuthentication(String userName) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        final String token = generateToken(userDetails);
        final String refreshToken = refreshToken(userDetails);
        final String fileToken = fileToken();
        redisTemplate.opsForValue().set(fileToken, userDetails);
        return new SecurityModel(token, refreshToken, fileToken);
    }

    private String generateToken(UserDetails user) {
        System.out.println("token for user " + user.getUsername() + " generated at: " + new Date());
        Algorithm algorithm = Algorithm.HMAC256(GlobalConstants.SECRET_KEY.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withAudience(user.getPassword())
                .withExpiresAt(new Date(System.currentTimeMillis() + GlobalConstants.ACCESS_TOKEN_EXPIRATION))
                .withClaim(GlobalConstants.CLAIM_NAME, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;
    }

    private String refreshToken(UserDetails user) {
        Algorithm algorithm = Algorithm.HMAC256(GlobalConstants.SECRET_KEY.getBytes());
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withAudience(user.getPassword())
                .withClaim(GlobalConstants.CLAIM_NAME, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withExpiresAt(new Date(System.currentTimeMillis() + GlobalConstants.REFRESH_TOKEN_EXPIRATION * 60 * 1000))
                .sign(algorithm);
        return refresh_token;
    }

    private String fileToken() {
        return UUID.randomUUID().toString();
    }


}
