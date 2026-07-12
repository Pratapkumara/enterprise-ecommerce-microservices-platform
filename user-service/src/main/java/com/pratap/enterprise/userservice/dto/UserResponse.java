package com.pratap.enterprise.userservice.dto;

import com.pratap.enterprise.userservice.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private User.Role role;

    private User.Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
