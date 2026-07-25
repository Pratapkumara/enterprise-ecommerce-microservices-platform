package com.pratap.enterprise.userservice.mapper;

import com.pratap.enterprise.userservice.dto.UserRequest;
import com.pratap.enterprise.userservice.dto.UserResponse;
import com.pratap.enterprise.userservice.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper =
            Mappers.getMapper(UserMapper.class);

    @Test
    void toEntityShouldMapRequestFields() {
        UserRequest request = UserRequest.builder()
                .firstName("Pratap")
                .lastName("Sahoo")
                .email("pratap@example.com")
                .password("password123")
                .phone("9876543210")
                .role(User.Role.ADMIN)
                .status(User.Status.ACTIVE)
                .build();

        User result = userMapper.toEntity(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getFirstName()).isEqualTo("Pratap");
        assertThat(result.getLastName()).isEqualTo("Sahoo");
        assertThat(result.getEmail())
                .isEqualTo("pratap@example.com");
        assertThat(result.getPassword())
                .isEqualTo("password123");
        assertThat(result.getPhone())
                .isEqualTo("9876543210");
        assertThat(result.getRole()).isEqualTo(User.Role.ADMIN);
        assertThat(result.getStatus())
                .isEqualTo(User.Status.ACTIVE);
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    void toEntityShouldReturnNullForNullRequest() {
        assertThat(userMapper.toEntity(null)).isNull();
    }

    @Test
    void toResponseShouldMapUserFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt =
                LocalDateTime.of(2026, 7, 25, 10, 30);
        LocalDateTime updatedAt =
                LocalDateTime.of(2026, 7, 25, 11, 30);

        User user = User.builder()
                .id(id)
                .firstName("Pratap")
                .lastName("Sahoo")
                .email("pratap@example.com")
                .password("encoded-password")
                .phone("9876543210")
                .role(User.Role.USER)
                .status(User.Status.ACTIVE)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        UserResponse result = userMapper.toResponse(user);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getFirstName()).isEqualTo("Pratap");
        assertThat(result.getLastName()).isEqualTo("Sahoo");
        assertThat(result.getEmail())
                .isEqualTo("pratap@example.com");
        assertThat(result.getPhone())
                .isEqualTo("9876543210");
        assertThat(result.getRole()).isEqualTo(User.Role.USER);
        assertThat(result.getStatus())
                .isEqualTo(User.Status.ACTIVE);
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void toResponseShouldReturnNullForNullUser() {
        assertThat(userMapper.toResponse(null)).isNull();
    }

    @Test
    void updateEntityShouldUpdateFieldsAndPreserveManagedFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt =
                LocalDateTime.of(2026, 7, 24, 10, 0);
        LocalDateTime updatedAt =
                LocalDateTime.of(2026, 7, 24, 11, 0);

        User existingUser = User.builder()
                .id(id)
                .firstName("Old")
                .lastName("Name")
                .email("old@example.com")
                .password("old-password")
                .phone("1111111111")
                .role(User.Role.USER)
                .status(User.Status.INACTIVE)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        UserRequest request = UserRequest.builder()
                .firstName("New")
                .lastName("User")
                .email("new@example.com")
                .password("new-password")
                .phone("9999999999")
                .role(User.Role.ADMIN)
                .status(User.Status.ACTIVE)
                .build();

        userMapper.updateEntity(request, existingUser);

        assertThat(existingUser.getId()).isEqualTo(id);
        assertThat(existingUser.getFirstName()).isEqualTo("New");
        assertThat(existingUser.getLastName()).isEqualTo("User");
        assertThat(existingUser.getEmail())
                .isEqualTo("new@example.com");
        assertThat(existingUser.getPassword())
                .isEqualTo("new-password");
        assertThat(existingUser.getPhone())
                .isEqualTo("9999999999");
        assertThat(existingUser.getRole())
                .isEqualTo(User.Role.ADMIN);
        assertThat(existingUser.getStatus())
                .isEqualTo(User.Status.ACTIVE);
        assertThat(existingUser.getCreatedAt())
                .isEqualTo(createdAt);
        assertThat(existingUser.getUpdatedAt())
                .isEqualTo(updatedAt);
    }

    @Test
    void updateEntityShouldDoNothingForNullRequest() {
        User existingUser = User.builder()
                .firstName("Existing")
                .email("existing@example.com")
                .build();

        userMapper.updateEntity(null, existingUser);

        assertThat(existingUser.getFirstName())
                .isEqualTo("Existing");
        assertThat(existingUser.getEmail())
                .isEqualTo("existing@example.com");
    }
}
