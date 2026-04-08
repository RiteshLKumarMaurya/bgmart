// src/main/java/com/brizerhero/lockeryard/dtos/TokenResponse.java
package com.biharigraphic.jilamart.auth.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    private long accessTokenExpiry;   // timestamp (ms)
    private long refreshTokenExpiry;  // timestamp (ms)
}