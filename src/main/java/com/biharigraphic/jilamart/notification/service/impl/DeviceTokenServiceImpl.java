package com.biharigraphic.jilamart.notification.service.impl;

import com.biharigraphic.jilamart.notification.entity.DeviceToken;
import com.biharigraphic.jilamart.notification.repository.DeviceTokenRepository;
import com.biharigraphic.jilamart.notification.service.DeviceTokenService;
import com.biharigraphic.jilamart.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceTokenServiceImpl implements DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    @Override
    @Transactional
    public void saveOrUpdate(User user, String token, String device) {

        if (token == null || token.isBlank()) return;

        DeviceToken existing = deviceTokenRepository.findByToken(token).orElse(null);

        if (existing == null) {
            DeviceToken newToken = new DeviceToken();
            newToken.setToken(token);
            newToken.setDevice(device != null ? device : "unknown");
            newToken.setUser(user);

            deviceTokenRepository.save(newToken);

        } else {
            // update mapping (important case)
            existing.setUser(user);
            existing.setDevice(device != null ? device : existing.getDevice());

            deviceTokenRepository.save(existing);
        }
    }

    @Override
    @Transactional
    public void removeToken(String token) {
        if (token == null || token.isBlank()) return;
        deviceTokenRepository.deleteByToken(token);
    }
}