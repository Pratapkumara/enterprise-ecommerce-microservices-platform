package com.pratap.enterprise.api_gateway.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private final JwtService jwtService =
            mock(JwtService.class);

    private final JwtAuthenticationFilter filter =
            new JwtAuthenticationFilter(jwtService);

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void requestWithoutBearerTokenShouldContinueUnauthenticated()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/v1/orders");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void validBearerTokenShouldAuthenticateRequest()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/v1/orders");

        request.addHeader(
                SecurityConstants.HEADER_NAME,
                SecurityConstants.TOKEN_PREFIX + "valid-token"
        );

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain = mock(FilterChain.class);

        when(jwtService.isTokenValid("valid-token"))
                .thenReturn(true);

        when(jwtService.extractUsername("valid-token"))
                .thenReturn("user@example.com");

        filter.doFilterInternal(request, response, filterChain);

        assertEquals(
                "user@example.com",
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void invalidBearerTokenShouldContinueUnauthenticated()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/v1/orders");

        request.addHeader(
                SecurityConstants.HEADER_NAME,
                SecurityConstants.TOKEN_PREFIX + "invalid-token"
        );

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain = mock(FilterChain.class);

        when(jwtService.isTokenValid("invalid-token"))
                .thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(jwtService, never())
                .extractUsername(anyString());

        verify(filterChain).doFilter(request, response);
    }
}
