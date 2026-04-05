package com.biharigraphic.jilamart.auth.controller;

import com.biharigraphic.jilamart.auth.dto.req.OtpLoginRequest;
import com.biharigraphic.jilamart.auth.dto.request.*;
import com.biharigraphic.jilamart.auth.dto.response.ChangePasswordResponse;
import com.biharigraphic.jilamart.auth.dto.response.ChangeUsernameResponse;
import com.biharigraphic.jilamart.auth.dto.response.RefreshTokenResponse;
import com.biharigraphic.jilamart.auth.dto.response.TokenResponse;
import com.biharigraphic.jilamart.auth.service.GoogleAuthService;
import com.biharigraphic.jilamart.auth.service.OtpAuthService;
import com.biharigraphic.jilamart.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;
    private final GoogleAuthService googleAuthService;


    /// OTP LOGIN
    private final OtpAuthService otpAuthService;

    @PostMapping("/otp-login")
    public ResponseEntity<TokenResponse> otpLogin(@RequestBody OtpLoginRequest request) {
        return ResponseEntity.ok(otpAuthService.loginWithOtp(request.getFirebaseToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String accessToken) {

        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7); // Remove "Bearer "
        }

        boolean valid = authService.validate(accessToken);
        return valid ? ResponseEntity.ok("Token valid") : ResponseEntity.status(401).body("Invalid token");
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }


    @PostMapping("/change-username")
    public ResponseEntity<ChangeUsernameResponse> changeUsername(@RequestBody ChangeUsernameRequest request) {
        return ResponseEntity.ok(authService.changeUsername(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(authService.changePassword(request));
    }

    @PostMapping("/google-login")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        return ResponseEntity.ok(googleAuthService.googleLogin(request));
    }
}