package com.pratap.enterprise.notificationservice.service;

import com.pratap.enterprise.notificationservice.dto.NotificationRequest;
import com.pratap.enterprise.notificationservice.dto.NotificationResponse;
import com.pratap.enterprise.notificationservice.entity.Notification;
import com.pratap.enterprise.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private NotificationRequest request;
    private Notification notification;

    @BeforeEach
    void setUp() {
        request = NotificationRequest.builder()
                .userId(10L)
                .email("user@example.com")
                .message("Order created successfully")
                .type("ORDER")
                .build();

        notification = Notification.builder()
                .id(1L)
                .userId(10L)
                .email("user@example.com")
                .message("Order created successfully")
                .type("ORDER")
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createNotificationShouldSaveAndReturnResponse() {
        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(notification);

        NotificationResponse response =
                notificationService.createNotification(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(10L, response.getUserId());
        assertEquals("user@example.com", response.getEmail());
        assertEquals(
                "Order created successfully",
                response.getMessage()
        );
        assertEquals("ORDER", response.getType());
        assertEquals("SENT", response.getStatus());
        assertEquals(
                notification.getCreatedAt(),
                response.getCreatedAt()
        );

        ArgumentCaptor<Notification> captor =
                ArgumentCaptor.forClass(Notification.class);

        verify(notificationRepository).save(captor.capture());

        Notification value = captor.getValue();

        assertEquals(10L, value.getUserId());
        assertEquals("user@example.com", value.getEmail());
        assertEquals(
                "Order created successfully",
                value.getMessage()
        );
        assertEquals("ORDER", value.getType());
        assertEquals("SENT", value.getStatus());
    }

    @Test
    void getNotificationByIdShouldReturnResponse() {
        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        NotificationResponse response =
                notificationService.getNotificationById(1L);

        assertEquals(1L, response.getId());
        assertEquals(10L, response.getUserId());
        assertEquals("user@example.com", response.getEmail());
        assertEquals("SENT", response.getStatus());

        verify(notificationRepository).findById(1L);
    }

    @Test
    void getNotificationByIdShouldThrowWhenMissing() {
        when(notificationRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.getNotificationById(99L)
        );

        assertEquals(
                "Notification not found with id: 99",
                exception.getMessage()
        );
    }

    @Test
    void getNotificationsByUserIdShouldReturnMappedList() {
        Notification second = Notification.builder()
                .id(2L)
                .userId(10L)
                .email("user@example.com")
                .message("Payment completed")
                .type("PAYMENT")
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();

        when(notificationRepository.findByUserId(10L))
                .thenReturn(List.of(notification, second));

        List<NotificationResponse> responses =
                notificationService.getNotificationsByUserId(10L);

        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals(2L, responses.get(1).getId());
        assertEquals("ORDER", responses.get(0).getType());
        assertEquals("PAYMENT", responses.get(1).getType());

        verify(notificationRepository).findByUserId(10L);
    }

    @Test
    void getNotificationsByUserIdShouldReturnEmptyList() {
        when(notificationRepository.findByUserId(55L))
                .thenReturn(List.of());

        List<NotificationResponse> responses =
                notificationService.getNotificationsByUserId(55L);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
}
