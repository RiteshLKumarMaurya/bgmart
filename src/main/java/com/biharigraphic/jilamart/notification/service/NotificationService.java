package com.biharigraphic.jilamart.notification.service;

import com.biharigraphic.jilamart.notification.entity.DeviceToken;
import com.biharigraphic.jilamart.notification.entity.Notification;
import com.biharigraphic.jilamart.notification.repo.DeviceTokenRepository;
import com.biharigraphic.jilamart.notification.repo.NotificationRepository;
import com.biharigraphic.jilamart.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final NotificationRepository notificationRepository;


    public void sendToAdmins(List<User> admins, String title, String message) {

        for (User admin : admins) {
            sendPush(admin.getFcmToken(), title, message);
        }
    }

    private void sendPush(String token, String title, String body) {
        // Firebase logic
    }

    public void sendToUser(User user, String title, String message) {

        List<DeviceToken> tokens = deviceTokenRepository.findByUser(user);

        for (DeviceToken dt : tokens) {
            sendPush(dt.getToken(), title, message);
        }

        // 🔹 DB save
        Notification n = new Notification();
        n.setUser(user);
        n.setTitle(title);
        n.setMessage(message);

        notificationRepository.save(n);
    }


}