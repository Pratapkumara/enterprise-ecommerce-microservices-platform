package com.pratap.enterprise.notificationservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private Long userId;

    private String email;

    private String message;

    private String type;

    private String status;

    private LocalDateTime createdAt;
}
