package com.biharigraphic.jilamart.auth.controller;

import com.biharigraphic.jilamart.auth.dto.req.OtpLoginRequest;
import com.biharigraphic.jilamart.auth.dto.request.*;
import com.biharigraphic.jilamart.auth.dto.response.*;
import com.biharigraphic.jilamart.auth.service.GoogleAuthService;
import com.biharigraphic.jilamart.auth.service.OtpAuthService;
import com.biharigraphic.jilamart.payload.ApiResponse;
import com.biharigraphic.jilamart.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final GoogleAuthService googleAuthService;
    private final OtpAuthService otpAuthService;

    // ✅ OTP LOGIN
    @PostMapping("/otp-login")
    public ResponseEntity<ApiResponse<TokenResponse>> otpLogin(@Valid @RequestBody OtpLoginRequest request) {
        TokenResponse response = otpAuthService.loginWithOtp(request.getFirebaseToken());

        return ResponseEntity.ok(
                ApiResponse.<TokenResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .build()
        );
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(null)
                        .build()
        );
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<TokenResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .build()
        );
    }

    // ✅ VALIDATE TOKEN
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validate(@Valid @RequestHeader("Authorization") String accessToken) {

        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        boolean valid = authService.validate(accessToken);

        return ResponseEntity.ok(
                ApiResponse.<Boolean>builder()
                        .success(valid)
                        .message(valid ? "Token valid" : "Invalid token")
                        .data(valid)
                        .build()
        );
    }

    // ✅ REFRESH TOKEN
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refresh(@Valid @RequestBody RefreshRequest request) {

        RefreshTokenResponse response = authService.refresh(request.getRefreshToken());

        return ResponseEntity.ok(
                ApiResponse.<RefreshTokenResponse>builder()
                        .success(true)
                        .message("Token refreshed successfully")
                        .data(response)
                        .build()
        );
    }

    // ✅ CHANGE USERNAME
    @PostMapping("/change-username")
    public ResponseEntity<ApiResponse<ChangeUsernameResponse>> changeUsername(@Valid @RequestBody ChangeUsernameRequest request) {

        ChangeUsernameResponse response = authService.changeUsername(request);

        return ResponseEntity.ok(
                ApiResponse.<ChangeUsernameResponse>builder()
                        .success(true)
                        .message("Username updated successfully")
                        .data(response)
                        .build()
        );
    }

    // ✅ CHANGE PASSWORD
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<ChangePasswordResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {

        ChangePasswordResponse response = authService.changePassword(request);

        return ResponseEntity.ok(
                ApiResponse.<ChangePasswordResponse>builder()
                        .success(true)
                        .message("Password updated successfully")
                        .data(response)
                        .build()
        );
    }

    // ✅ GOOGLE LOGIN
    @PostMapping("/google-login")
    public ResponseEntity<ApiResponse<TokenResponse>> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {

        TokenResponse response = googleAuthService.googleLogin(request);

        return ResponseEntity.ok(
                ApiResponse.<TokenResponse>builder()
                        .success(true)
                        .message("Google login successful")
                        .data(response)
                        .build()
        );
    }
}