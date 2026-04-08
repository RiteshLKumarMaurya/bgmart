package com.biharigraphic.jilamart.notification.service;

import com.biharigraphic.jilamart.notification.dto.NotificationRequest;
import com.biharigraphic.jilamart.notification.dto.NotificationResponse;
import com.biharigraphic.jilamart.notification.enums.NotificationType;
import com.biharigraphic.jilamart.user.entity.User;

import java.util.List;
public interface NotificationService {

    void createNotification(NotificationRequest request);

    List<NotificationResponse> getUserNotifications(User user);

    void markAsRead(Long notificationId, User user);

    long getUnreadCount(User user);

    void notifyLogin(User user, String currentToken);

    // 🔥 NEW METHODS
    void sendToUser(User user, String title, String message);

    void sendToUser(User user, String title, String message, String imageUrl, String redirectUrl, NotificationType notificationType);
}