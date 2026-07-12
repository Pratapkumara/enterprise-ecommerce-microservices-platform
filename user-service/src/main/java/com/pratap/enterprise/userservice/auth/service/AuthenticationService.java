package com.pratap.enterprise.userservice.auth.service;

import com.pratap.enterprise.userservice.auth.dto.LoginRequest;
import com.pratap.enterprise.userservice.auth.dto.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);

}
