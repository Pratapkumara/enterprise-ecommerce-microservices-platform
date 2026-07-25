package com.pratap.enterprise.userservice.auth.service.impl;

import com.pratap.enterprise.userservice.auth.dto.LoginRequest;
import com.pratap.enterprise.userservice.auth.dto.LoginResponse;
import com.pratap.enterprise.userservice.security.CustomUserDetailsService;
import com.pratap.enterprise.userservice.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("pratap@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void loginShouldAuthenticateUserAndReturnJwtToken() {
        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(customUserDetailsService.loadUserByUsername(
                "pratap@example.com"
        )).thenReturn(userDetails);

        when(jwtService.generateToken(userDetails))
                .thenReturn("generated-jwt-token");

        LoginResponse response =
                authenticationService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken())
                .isEqualTo("generated-jwt-token");
        assertThat(response.getTokenType())
                .isEqualTo("Bearer");

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(
                        UsernamePasswordAuthenticationToken.class
                );

        verify(authenticationManager).authenticate(captor.capture());

        assertThat(captor.getValue().getPrincipal())
                .isEqualTo("pratap@example.com");
        assertThat(captor.getValue().getCredentials())
                .isEqualTo("password123");

        verify(customUserDetailsService)
                .loadUserByUsername("pratap@example.com");
        verify(jwtService).generateToken(userDetails);
    }
}
