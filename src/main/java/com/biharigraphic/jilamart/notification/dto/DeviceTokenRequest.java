package com.biharigraphic.jilamart.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceTokenRequest {

    private String token;
    private String device; // android / ios / web
}