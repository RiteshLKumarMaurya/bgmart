package com.biharigraphic.jilamart.notification.controller;

import com.biharigraphic.jilamart.notification.dto.DeviceTokenRequest;
import com.biharigraphic.jilamart.notification.service.DeviceTokenService;
import com.biharigraphic.jilamart.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/device-token")
@RequiredArgsConstructor
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    @PostMapping
    public void saveToken(@AuthenticationPrincipal User user,
                          @RequestBody DeviceTokenRequest request) {

        deviceTokenService.saveOrUpdate(user, request.getToken(), request.getDevice());
    }

    @DeleteMapping
    public void removeToken(@RequestParam String token) {
        deviceTokenService.removeToken(token);
    }
}