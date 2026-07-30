package com.pratap.enterprise.api_gateway.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConstantsTest {

    @Test
    void constantsShouldContainExpectedSecurityValues() {
        assertEquals(
                "Bearer ",
                SecurityConstants.TOKEN_PREFIX
        );

        assertEquals(
                "Authorization",
                SecurityConstants.HEADER_NAME
        );

        assertArrayEquals(
                new String[]{
                        "/api/v1/auth/**",
                        "/api/v1/users/register",
                        "/actuator/**"
                },
                SecurityConstants.PUBLIC_URLS
        );
    }
}
