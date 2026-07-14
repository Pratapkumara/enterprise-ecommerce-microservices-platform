package com.pratap.enterprise.notificationservice.service;

import com.pratap.enterprise.notificationservice.dto.NotificationRequest;
import com.pratap.enterprise.notificationservice.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    NotificationResponse getNotificationById(Long id);

    List<NotificationResponse> getNotificationsByUserId(Long userId);
}
