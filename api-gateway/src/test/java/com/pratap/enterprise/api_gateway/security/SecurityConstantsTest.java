package com.pratap.enterprise.api_gateway.security;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        assertEquals(
                List.of(
                        "/api/v1/auth/**",
                        "/api/v1/users/register",
                        "/actuator/**"
                ),
                SecurityConstants.PUBLIC_URLS
        );
    }

    @Test
    void publicUrlsShouldBeImmutable() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> SecurityConstants.PUBLIC_URLS.add(
                        "/unsafe"
                )
        );
    }
}
