package com.biharigraphic.jilamart.notification.controller;

import com.biharigraphic.jilamart.notification.dto.NotificationResponse;
import com.biharigraphic.jilamart.notification.service.NotificationService;
import com.biharigraphic.jilamart.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> getMyNotifications(@AuthenticationPrincipal User user) {
        return notificationService.getUserNotifications(user);
    }

    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id,
                           @AuthenticationPrincipal User user) {
        notificationService.markAsRead(id, user);
    }

    @GetMapping("/unread-count")
    public long getUnreadCount(@AuthenticationPrincipal User user) {
        return notificationService.getUnreadCount(user);
    }
}