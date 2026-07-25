package com.pratap.enterprise.userservice.service.impl;

import com.pratap.enterprise.userservice.dto.UserRequest;
import com.pratap.enterprise.userservice.dto.UserResponse;
import com.pratap.enterprise.userservice.entity.User;
import com.pratap.enterprise.userservice.exception.ResourceNotFoundException;
import com.pratap.enterprise.userservice.mapper.UserMapper;
import com.pratap.enterprise.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        userRequest = UserRequest.builder()
                .firstName("Pratap")
                .lastName("Sahoo")
                .email("pratap@example.com")
                .password("password123")
                .phone("9876543210")
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .build();

        user = User.builder()
                .id(userId)
                .firstName("Pratap")
                .lastName("Sahoo")
                .email("pratap@example.com")
                .password("encoded-password")
                .phone("9876543210")
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userResponse = UserResponse.builder()
                .id(userId)
                .firstName("Pratap")
                .lastName("Sahoo")
                .email("pratap@example.com")
                .phone("9876543210")
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Test
    void createUserShouldEncodePasswordSaveAndReturnResponse() {
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createUser(userRequest);

        assertThat(result).isEqualTo(userResponse);
        assertThat(user.getPassword()).isEqualTo("encoded-password");

        verify(userMapper).toEntity(userRequest);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getUserByIdShouldReturnResponseWhenUserExists() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(userId);

        assertThat(result).isEqualTo(userResponse);

        verify(userRepository).findById(userId);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with id : " + userId);

        verify(userRepository).findById(userId);
        verify(userMapper, never()).toResponse(user);
    }

    @Test
    void getAllUsersShouldReturnMappedResponses() {
        User secondUser = User.builder()
                .id(UUID.randomUUID())
                .firstName("DevOps")
                .lastName("Engineer")
                .email("devops@example.com")
                .password("encoded-password")
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .build();

        UserResponse secondResponse = UserResponse.builder()
                .id(secondUser.getId())
                .firstName("DevOps")
                .lastName("Engineer")
                .email("devops@example.com")
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .build();

        when(userRepository.findAll())
                .thenReturn(List.of(user, secondUser));
        when(userMapper.toResponse(user)).thenReturn(userResponse);
        when(userMapper.toResponse(secondUser))
                .thenReturn(secondResponse);

        List<UserResponse> result = userService.getAllUsers();

        assertThat(result)
                .containsExactly(userResponse, secondResponse);

        verify(userRepository).findAll();
        verify(userMapper).toResponse(user);
        verify(userMapper).toResponse(secondUser);
    }

    @Test
    void getAllUsersShouldReturnEmptyListWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponse> result = userService.getAllUsers();

        assertThat(result).isEmpty();

        verify(userRepository).findAll();
    }

    @Test
    void updateUserShouldEncodeNewPasswordAndReturnResponse() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.encode("password123"))
                .thenReturn("new-encoded-password");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result =
                userService.updateUser(userId, userRequest);

        assertThat(result).isEqualTo(userResponse);
        assertThat(user.getPassword())
                .isEqualTo("new-encoded-password");

        verify(userMapper).updateEntity(userRequest, user);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);
    }

    @Test
    void updateUserShouldNotEncodePasswordWhenPasswordIsBlank() {
        userRequest.setPassword(" ");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result =
                userService.updateUser(userId, userRequest);

        assertThat(result).isEqualTo(userResponse);

        verify(userMapper).updateEntity(userRequest, user);
        verify(passwordEncoder, never()).encode(" ");
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);
    }

    @Test
    void updateUserShouldNotEncodePasswordWhenPasswordIsNull() {
        userRequest.setPassword(null);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result =
                userService.updateUser(userId, userRequest);

        assertThat(result).isEqualTo(userResponse);

        verify(userMapper).updateEntity(userRequest, user);
        verify(passwordEncoder, never()).encode(
                org.mockito.ArgumentMatchers.anyString()
        );
        verify(userRepository).save(user);
    }

    @Test
    void updateUserShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> userService.updateUser(userId, userRequest)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with id : " + userId);

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(user);
    }

    @Test
    void deleteUserShouldDeleteExistingUser() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUserShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with id : " + userId);

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(user);
    }
}
