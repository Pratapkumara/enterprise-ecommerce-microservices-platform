package com.pratap.enterprise.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        userDetails = User.builder()
                .username("pratap@example.com")
                .password("encoded-password")
                .roles("USER")
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterWhenAuthorizationHeaderIsMissing()
            throws Exception {

        when(request.getHeader(SecurityConstants.HEADER_NAME))
                .thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never())
                .extractUsername(anyString());

        assertThat(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        ).isNull();
    }

    @Test
    void shouldContinueFilterWhenHeaderDoesNotStartWithBearer()
            throws Exception {

        when(request.getHeader(SecurityConstants.HEADER_NAME))
                .thenReturn("Basic credentials");

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never())
                .extractUsername(anyString());

        assertThat(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        ).isNull();
    }

    @Test
    void shouldAuthenticateWhenJwtTokenIsValid()
            throws Exception {

        when(request.getHeader(SecurityConstants.HEADER_NAME))
                .thenReturn("Bearer valid-token");

        when(jwtService.extractUsername("valid-token"))
                .thenReturn("pratap@example.com");

        when(customUserDetailsService.loadUserByUsername(
                "pratap@example.com"
        )).thenReturn(userDetails);

        when(jwtService.isTokenValid(
                "valid-token",
                userDetails
        )).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertThat(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        ).isNotNull();

        assertThat(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        ).isEqualTo("pratap@example.com");

        verify(customUserDetailsService)
                .loadUserByUsername("pratap@example.com");

        verify(jwtService)
                .isTokenValid("valid-token", userDetails);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenJwtTokenIsInvalid()
            throws Exception {

        when(request.getHeader(SecurityConstants.HEADER_NAME))
                .thenReturn("Bearer invalid-token");

        when(jwtService.extractUsername("invalid-token"))
                .thenReturn("pratap@example.com");

        when(customUserDetailsService.loadUserByUsername(
                "pratap@example.com"
        )).thenReturn(userDetails);

        when(jwtService.isTokenValid(
                "invalid-token",
                userDetails
        )).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        assertThat(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        ).isNull();

        verify(customUserDetailsService)
                .loadUserByUsername("pratap@example.com");

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotLoadUserWhenAuthenticationAlreadyExists()
            throws Exception {

        UsernamePasswordAuthenticationToken existingAuthentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(existingAuthentication);

        when(request.getHeader(SecurityConstants.HEADER_NAME))
                .thenReturn("Bearer existing-token");

        when(jwtService.extractUsername("existing-token"))
                .thenReturn("pratap@example.com");

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(customUserDetailsService, never())
                .loadUserByUsername(anyString());

        verify(jwtService, never())
                .isTokenValid(
                        org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers
                                .any(UserDetails.class)
                );

        assertThat(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        ).isSameAs(existingAuthentication);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotLoadUserWhenUsernameIsMissing()
            throws Exception {

        when(request.getHeader(SecurityConstants.HEADER_NAME))
                .thenReturn("Bearer subjectless-token");

        when(jwtService.extractUsername("subjectless-token"))
                .thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(customUserDetailsService, never())
                .loadUserByUsername(anyString());

        verify(jwtService, never())
                .isTokenValid(
                        org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers
                                .any(UserDetails.class)
                );

        assertThat(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        ).isNull();

        verify(filterChain).doFilter(request, response);
    }
}
