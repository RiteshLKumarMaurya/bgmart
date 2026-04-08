package com.biharigraphic.jilamart.notification.service;

import com.biharigraphic.jilamart.user.entity.User;

public interface DeviceTokenService {

    void saveOrUpdate(User user, String token, String device);

    void removeToken(String token);
}