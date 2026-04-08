package com.biharigraphic.jilamart.notification.repository;

import com.biharigraphic.jilamart.notification.entity.DeviceToken;
import com.biharigraphic.jilamart.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByToken(String token);

    List<DeviceToken> findByUser(User user);

    void deleteByToken(String token);
}