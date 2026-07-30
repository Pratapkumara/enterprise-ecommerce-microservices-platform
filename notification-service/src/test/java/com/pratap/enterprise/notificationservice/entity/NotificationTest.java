package com.pratap.enterprise.notificationservice.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void prePersistShouldSetCreationTimestamp() {
        Notification notification = Notification.builder()
                .userId(10L)
                .email("user@example.com")
                .message("Test notification")
                .type("ORDER")
                .status("SENT")
                .build();

        assertNull(notification.getCreatedAt());

        notification.prePersist();

        assertNotNull(notification.getCreatedAt());
    }
}
