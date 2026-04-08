package com.biharigraphic.jilamart.notification.dto;

import com.biharigraphic.jilamart.notification.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {

    private String title;
    private String message;
    private NotificationType notificationType;

    private String imageUrl;
    private String redirectUrl;

    private Long userId; // null = broadcast
}