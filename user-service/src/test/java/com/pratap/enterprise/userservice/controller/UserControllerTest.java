package com.pratap.enterprise.userservice.controller;

import com.pratap.enterprise.userservice.dto.UserRequest;
import com.pratap.enterprise.userservice.dto.UserResponse;
import com.pratap.enterprise.userservice.entity.User;
import com.pratap.enterprise.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private UserRequest request;
    private UserResponse response;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        request = UserRequest.builder()
                .firstName("Pratap")
                .lastName("Sahoo")
                .email("pratap@example.com")
                .password("password123")
                .phone("9876543210")
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .build();

        response = UserResponse.builder()
                .id(userId)
                .firstName("Pratap")
                .lastName("Sahoo")
                .email("pratap@example.com")
                .phone("9876543210")
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .build();
    }

    @Test
    void registerUserShouldReturnCreatedUser() {
        when(userService.createUser(request)).thenReturn(response);

        UserResponse result = userController.registerUser(request);

        assertThat(result).isSameAs(response);
        verify(userService).createUser(request);
    }

    @Test
    void getUserByIdShouldReturnUser() {
        when(userService.getUserById(userId)).thenReturn(response);

        UserResponse result = userController.getUserById(userId);

        assertThat(result).isSameAs(response);
        verify(userService).getUserById(userId);
    }

    @Test
    void getAllUsersShouldReturnUsers() {
        when(userService.getAllUsers())
                .thenReturn(List.of(response));

        List<UserResponse> result =
                userController.getAllUsers();

        assertThat(result).containsExactly(response);
        verify(userService).getAllUsers();
    }

    @Test
    void updateUserShouldReturnUpdatedUser() {
        when(userService.updateUser(userId, request))
                .thenReturn(response);

        UserResponse result =
                userController.updateUser(userId, request);

        assertThat(result).isSameAs(response);
        verify(userService).updateUser(userId, request);
    }

    @Test
    void deleteUserShouldCallService() {
        userController.deleteUser(userId);

        verify(userService).deleteUser(userId);
    }
}
