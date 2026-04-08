package com.biharigraphic.jilamart.notification.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DeviceToken extends BaseEntity {

    @Column(unique = true)
    private String token;

    private String device; // mobile, web

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}