package com.biharigraphic.jilamart.notification.controller;

import com.biharigraphic.jilamart.notification.entity.Notification;
import com.biharigraphic.jilamart.notification.repo.NotificationRepository;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @PostMapping("/read/{id}")
    public String markAsRead(@PathVariable Long id, Authentication auth) {

        // 🔐 Logged-in user
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        User user = userRepository.findByPhoneNumber(userDetails.getUsername())
                .orElseThrow();

        // 🔍 Notification fetch
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        // 🚨 Security check
        if (!n.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access ❌");
        }

        // ✅ Mark as read
        n.setRead(true);
        notificationRepository.save(n);

        return "Marked as read ✅";
    }

    @PostMapping("/read-all")
    public String markAllAsRead(Authentication auth) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        User user = userRepository.findByPhoneNumber(userDetails.getUsername())
                .orElseThrow();

        List<Notification> list = notificationRepository.findByUserAndReadFalse(user);

        list.forEach(n -> n.setRead(true));

        notificationRepository.saveAll(list);

        return "All marked as read ✅";
    }
}