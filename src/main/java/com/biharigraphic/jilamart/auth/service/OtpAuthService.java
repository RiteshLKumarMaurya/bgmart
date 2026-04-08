package com.biharigraphic.jilamart.auth.service;

import com.biharigraphic.jilamart.auth.dto.response.TokenResponse;

public interface OtpAuthService {

    TokenResponse loginWithOtp(String firebaseToken);
}