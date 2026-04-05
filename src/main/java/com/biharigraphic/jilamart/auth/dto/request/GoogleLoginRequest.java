// src/main/java/com/brizerhero/BrizerHero/auth/dto/request/GoogleLoginRequest.java
package com.biharigraphic.jilamart.auth.dto.request;

import lombok.Data;

@Data
public class GoogleLoginRequest {
    private String idToken;// token you get from Android Google Sign-In
    private String Role;
}
