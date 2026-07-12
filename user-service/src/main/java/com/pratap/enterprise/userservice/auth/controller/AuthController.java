package com.pratap.enterprise.userservice.auth.controller;

import com.pratap.enterprise.userservice.auth.dto.LoginRequest;
import com.pratap.enterprise.userservice.auth.dto.LoginResponse;
import com.pratap.enterprise.userservice.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authenticationService.login(request);
    }
}
