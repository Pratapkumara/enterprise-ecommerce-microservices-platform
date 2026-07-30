package com.pratap.enterprise.notificationservice.controller;

import com.pratap.enterprise.notificationservice.dto.NotificationRequest;
import com.pratap.enterprise.notificationservice.dto.NotificationResponse;
import com.pratap.enterprise.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController controller;

    private NotificationRequest request;
    private NotificationResponse response;

    @BeforeEach
    void setUp() {
        request = NotificationRequest.builder()
                .userId(10L)
                .email("user@example.com")
                .message("Order created")
                .type("ORDER")
                .build();

        response = NotificationResponse.builder()
                .id(1L)
                .userId(10L)
                .email("user@example.com")
                .message("Order created")
                .type("ORDER")
                .status("SENT")
                .build();
    }

    @Test
    void createNotificationShouldReturnCreatedNotification() {
        when(notificationService.createNotification(request))
                .thenReturn(response);

        NotificationResponse result =
                controller.createNotification(request);

        assertSame(response, result);
        verify(notificationService).createNotification(request);
    }

    @Test
    void getNotificationByIdShouldReturnNotification() {
        when(notificationService.getNotificationById(1L))
                .thenReturn(response);

        NotificationResponse result =
                controller.getNotificationById(1L);

        assertSame(response, result);
        verify(notificationService).getNotificationById(1L);
    }

    @Test
    void getNotificationsByUserIdShouldReturnList() {
        when(notificationService.getNotificationsByUserId(10L))
                .thenReturn(List.of(response));

        List<NotificationResponse> result =
                controller.getNotificationsByUserId(10L);

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
        verify(notificationService)
                .getNotificationsByUserId(10L);
    }
}
