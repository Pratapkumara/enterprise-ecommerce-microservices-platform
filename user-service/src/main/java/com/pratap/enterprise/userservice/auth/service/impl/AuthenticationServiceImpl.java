package com.pratap.enterprise.userservice.auth.service.impl;

import com.pratap.enterprise.userservice.auth.dto.LoginRequest;
import com.pratap.enterprise.userservice.auth.dto.LoginResponse;
import com.pratap.enterprise.userservice.auth.service.AuthenticationService;
import com.pratap.enterprise.userservice.security.CustomUserDetailsService;
import com.pratap.enterprise.userservice.security.JwtService;
import com.pratap.enterprise.userservice.security.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(
                        request.getEmail()
                );

        String token = jwtService.generateToken(userDetails);

        return LoginResponse.builder()
                .token(token)
                .tokenType(SecurityConstants.TOKEN_PREFIX.trim())
                .build();
    }
}
