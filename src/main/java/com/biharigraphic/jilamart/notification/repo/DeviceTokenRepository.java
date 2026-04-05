package com.biharigraphic.jilamart.notification.repo;

import com.biharigraphic.jilamart.notification.entity.DeviceToken;
import com.biharigraphic.jilamart.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    List<DeviceToken> findByUser(User user);
}