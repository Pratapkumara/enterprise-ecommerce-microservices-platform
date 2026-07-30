package com.pratap.enterprise.userservice.security;

import java.util.List;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String TOKEN_PREFIX =
            "Bearer ";

    public static final String HEADER_NAME =
            "Authorization";

    public static final List<String> PUBLIC_URLS =
            List.of(
                    "/error",
                    "/actuator/**",
                    "/health",
                    "/api/v1/auth/**",
                    "/api/v1/users/register"
            );
}
