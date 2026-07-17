package com.pratap.enterprise.orderservice.client;

import com.pratap.enterprise.orderservice.client.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "notification-service")
public interface NotificationClient {


    @PostMapping("/api/notifications")
    void sendNotification(
            @RequestBody NotificationRequest request
    );

}
