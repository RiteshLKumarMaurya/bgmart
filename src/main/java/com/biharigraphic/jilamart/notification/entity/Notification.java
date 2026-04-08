package com.biharigraphic.jilamart.notification.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.notification.enums.NotificationType;
import com.biharigraphic.jilamart.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Notification extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // null = broadcast

    private String title;
    private String message;

    @Column(name = "is_read")
    private Boolean read = false;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String imageUrl;
    private String redirectUrl; // deep link

}