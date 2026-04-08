package com.biharigraphic.jilamart.notification.dto;

import com.biharigraphic.jilamart.notification.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class NotificationResponse {

    private Long id;
    private String title;
    private String message;
    private Boolean read;
    private NotificationType notificationType;

    private String imageUrl;
    private String redirectUrl;

    private Instant createdAt;
}