package com.pratap.enterprise.api_gateway.security;

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
                    "/api/v1/auth/**",
                    "/api/v1/users/register",
                    "/actuator/**"
            );
}
