package com.pratap.enterprise.userservice.service;

import com.pratap.enterprise.userservice.dto.UserRequest;
import com.pratap.enterprise.userservice.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(UUID id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(UUID id, UserRequest request);

    void deleteUser(UUID id);
}
