package com.pratap.enterprise.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {

    private Long userId;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String message;

    @NotBlank
    private String type;
}
