package com.example.ertebatfardaboarding.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SecurityModel {
    Object accessToken, refreshToken;

    public SecurityModel() {
    }

    public SecurityModel(Object accessToken, Object refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Object getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Object accessToken) {
        this.accessToken = accessToken;
    }

    public Object getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(Object refreshToken) {
        this.refreshToken = refreshToken;
    }
}
