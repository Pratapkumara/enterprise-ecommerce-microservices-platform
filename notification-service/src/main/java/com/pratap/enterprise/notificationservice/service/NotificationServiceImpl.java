package com.pratap.enterprise.notificationservice.service;

import com.pratap.enterprise.notificationservice.dto.NotificationRequest;
import com.pratap.enterprise.notificationservice.dto.NotificationResponse;
import com.pratap.enterprise.notificationservice.entity.Notification;
import com.pratap.enterprise.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;


    @Override
    public NotificationResponse createNotification(NotificationRequest request) {

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .message(request.getMessage())
                .type(request.getType())
                .status("SENT")
                .build();

        Notification saved = notificationRepository.save(notification);

        return mapToResponse(saved);
    }


    @Override
    public NotificationResponse getNotificationById(Long id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found with id: " + id)
                );

        return mapToResponse(notification);
    }


    @Override
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {

        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private NotificationResponse mapToResponse(Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .email(notification.getEmail())
                .message(notification.getMessage())
                .type(notification.getType())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
