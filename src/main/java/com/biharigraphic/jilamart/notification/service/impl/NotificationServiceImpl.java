package com.biharigraphic.jilamart.notification.service.impl;

import com.biharigraphic.jilamart.notification.dto.NotificationRequest;
import com.biharigraphic.jilamart.notification.dto.NotificationResponse;
import com.biharigraphic.jilamart.notification.entity.DeviceToken;
import com.biharigraphic.jilamart.notification.entity.Notification;
import com.biharigraphic.jilamart.notification.enums.NotificationType;
import com.biharigraphic.jilamart.notification.repository.DeviceTokenRepository;
import com.biharigraphic.jilamart.notification.repository.NotificationRepository;
import com.biharigraphic.jilamart.notification.service.NotificationService;
import com.biharigraphic.jilamart.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    // 🔥 Create + Save
    @Override
    public void createNotification(NotificationRequest request) {

        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setNotificationType(request.getNotificationType());
        notification.setImageUrl(request.getImageUrl());
        notification.setRedirectUrl(request.getRedirectUrl());
        notification.setRead(false);
        notification.setCreatedAt(Instant.now());

        notificationRepository.save(notification);
    }

    // 📥 Get all
    @Override
    public List<NotificationResponse> getUserNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(n -> NotificationResponse.builder()
                        .id(n.getId())
                        .title(n.getTitle())
                        .message(n.getMessage())
                        .read(n.getRead())
                        .notificationType(n.getNotificationType())
                        .imageUrl(n.getImageUrl())
                        .redirectUrl(n.getRedirectUrl())
                        .createdAt(n.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());
    }

    // ✅ Mark read
    @Override
    public void markAsRead(Long id, User user) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow();

        if (!n.getUser().getId().equals(user.getId())) return;

        n.setRead(true);
        notificationRepository.save(n);
    }

    // 🔢 Unread count
    @Override
    public long getUnreadCount(User user) {
        return notificationRepository.countByUserAndReadFalse(user);
    }

    // 🔥 Login notify (other devices)
    @Override
    public void notifyLogin(User user, String currentToken) {

        List<DeviceToken> tokens = deviceTokenRepository.findByUser(user);

        for (DeviceToken t : tokens) {

            if (currentToken != null && currentToken.equals(t.getToken())) continue;

            // 👉 yaha FCM service call karna hai
            System.out.println("Send push to: " + t.getToken());
        }

        // DB save
        Notification n = new Notification();
        n.setUser(user);
        n.setTitle("New Login Detected");
        n.setMessage("Your account was accessed from a new device");
        n.setRead(false);
        n.setCreatedAt(Instant.now());

        notificationRepository.save(n);
    }

    @Override
    public void sendToUser(User user, String title, String message) {
        sendToUser(user, title, message, null, null,null);
    }

    @Override
    public void sendToUser(User user,
                           String title,
                           String message,
                           String imageUrl,
                           String redirectUrl,
                           NotificationType notificationType) {

        // ✅ 1. Save in DB
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setImageUrl(imageUrl);
        notification.setRedirectUrl(redirectUrl);
        notification.setNotificationType(null); // optional
        notification.setCreatedAt(Instant.now());

        notificationRepository.save(notification);

        // ✅ 2. Send to all devices
        List<DeviceToken> tokens = deviceTokenRepository.findByUser(user);

        for (DeviceToken t : tokens) {

            try {
                // 👉 yaha actual FCM call aayega
                System.out.println("Push → " + t.getToken());

            } catch (Exception e) {
                // ❗ optional: invalid token remove
                // deviceTokenRepository.delete(t);
            }
        }
    }



}