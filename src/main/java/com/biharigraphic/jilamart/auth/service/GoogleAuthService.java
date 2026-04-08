package com.biharigraphic.jilamart.auth.service;

import com.biharigraphic.jilamart.auth.dto.request.GoogleLoginRequest;
import com.biharigraphic.jilamart.auth.dto.response.TokenResponse;
import org.springframework.stereotype.Service;

public interface GoogleAuthService {

    TokenResponse googleLogin(GoogleLoginRequest request);
}