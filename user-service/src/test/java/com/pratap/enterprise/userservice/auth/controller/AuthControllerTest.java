package com.pratap.enterprise.userservice.auth.controller;

import com.pratap.enterprise.userservice.auth.dto.LoginRequest;
import com.pratap.enterprise.userservice.auth.dto.LoginResponse;
import com.pratap.enterprise.userservice.auth.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginShouldReturnAuthenticationResponse() {
        LoginRequest request = new LoginRequest();
        request.setEmail("pratap@example.com");
        request.setPassword("password123");

        LoginResponse expectedResponse = LoginResponse.builder()
                .token("jwt-token")
                .tokenType("Bearer")
                .build();

        when(authenticationService.login(request))
                .thenReturn(expectedResponse);

        LoginResponse result = authController.login(request);

        assertThat(result).isSameAs(expectedResponse);
        assertThat(result.getToken()).isEqualTo("jwt-token");
        assertThat(result.getTokenType()).isEqualTo("Bearer");

        verify(authenticationService).login(request);
    }
}
