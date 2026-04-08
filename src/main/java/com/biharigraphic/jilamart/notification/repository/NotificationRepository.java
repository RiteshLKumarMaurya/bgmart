package com.biharigraphic.jilamart.notification.repository;

import com.biharigraphic.jilamart.notification.entity.Notification;
import com.biharigraphic.jilamart.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    Page<Notification> findByUser(User user, Pageable pageable);

    List<Notification> findByUserAndReadFalse(User user);

    long countByUserAndReadFalse(User user);

    List<Notification> findByUserIsNull(); // broadcast

}