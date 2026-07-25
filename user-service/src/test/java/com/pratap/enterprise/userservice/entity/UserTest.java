package com.pratap.enterprise.userservice.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void prePersistShouldSetTimestampsAndDefaultValues() {
        User user = User.builder()
                .firstName("Pratap")
                .lastName("Sahoo")
                .email("pratap@example.com")
                .password("password")
                .build();

        LocalDateTime before = LocalDateTime.now();

        user.prePersist();

        assertThat(user.getCreatedAt())
                .isAfterOrEqualTo(before);
        assertThat(user.getUpdatedAt())
                .isAfterOrEqualTo(before);
        assertThat(user.getRole()).isEqualTo(User.Role.USER);
        assertThat(user.getStatus())
                .isEqualTo(User.Status.ACTIVE);
    }

    @Test
    void prePersistShouldPreserveExistingRoleAndStatus() {
        User user = User.builder()
                .role(User.Role.ADMIN)
                .status(User.Status.INACTIVE)
                .build();

        user.prePersist();

        assertThat(user.getRole()).isEqualTo(User.Role.ADMIN);
        assertThat(user.getStatus())
                .isEqualTo(User.Status.INACTIVE);
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    void preUpdateShouldRefreshUpdatedTimestamp() {
        User user = new User();

        LocalDateTime oldTimestamp =
                LocalDateTime.of(2026, 7, 24, 10, 0);

        user.setUpdatedAt(oldTimestamp);

        user.preUpdate();

        assertThat(user.getUpdatedAt())
                .isAfter(oldTimestamp);
    }
}
