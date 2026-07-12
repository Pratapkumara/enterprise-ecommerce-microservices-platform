package com.pratap.enterprise.userservice.security;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_NAME = "Authorization";

    public static final String[] PUBLIC_URLS = {
            "/actuator/**",
            "/health",
            "/api/v1/auth/**",
            "/api/v1/users/register"
    };

}
