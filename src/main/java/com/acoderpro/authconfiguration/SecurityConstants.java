package com.acoderpro.authconfiguration;

public class SecurityConstants {
    public static final String[] PUBLIC_URLS = {
        "/auth/login",
        "/auth/register",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-ui",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-ui/index.html",
        "/api/v1/account/create-user",
        "/api/v1/notification/forgot-pass",
        "/api/v1/account/otp-verify",
        "/api/v1/account/reset-pass",
    };
}
