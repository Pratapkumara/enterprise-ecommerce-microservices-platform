package com.pratap.enterprise.api_gateway.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private final JwtAuthenticationFilter filter =
            new JwtAuthenticationFilter();

    @Test
    void shouldNotFilterShouldReturnTrue() {
        MockHttpServletRequest request =
                new MockHttpServletRequest(
                        "GET",
                        "/api/products"
                );

        assertTrue(filter.shouldNotFilter(request));
    }

    @Test
    void doFilterInternalShouldContinueFilterChain()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest(
                        "GET",
                        "/api/products"
                );

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain filterChain =
                mock(FilterChain.class);

        filter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);
    }
}
