package com.example.ertebatfardaboarding.security;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SecurityModel {
    Object accessToken, refreshToken, fileToken;

    public SecurityModel() {
    }

    public SecurityModel(Object accessToken, Object refreshToken, Object fileToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.fileToken = fileToken;
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

    public Object getFileToken() {
        return fileToken;
    }

    public void setFileToken(Object fileToken) {
        this.fileToken = fileToken;
    }
}
