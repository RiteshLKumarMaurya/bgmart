package com.biharigraphic.jilamart.auth.controller;

import com.biharigraphic.jilamart.auth.dto.*;
import com.biharigraphic.jilamart.auth.dto.req.OtpLoginRequest;
import com.biharigraphic.jilamart.auth.dto.request.*;
import com.biharigraphic.jilamart.auth.dto.response.*;
import com.biharigraphic.jilamart.auth.service.impl.GoogleAuthServiceImpl;
import com.biharigraphic.jilamart.auth.service.impl.OtpAuthServiceImpl;
import com.biharigraphic.jilamart.enums.TokenType;
import com.biharigraphic.jilamart.exception.InvalidOrExpiredTokenException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;
import com.biharigraphic.jilamart.payload.ApiResponse;
import com.biharigraphic.jilamart.profile.controller.ProfileController;
import com.biharigraphic.jilamart.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final ProfileController profileController;

    private final AuthService authService;
    private final GoogleAuthServiceImpl googleAuthService;
    private final OtpAuthServiceImpl otpAuthService;

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
    public ResponseEntity<ApiResponse<PhonePasswordRegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        PhonePasswordRegisterResponse response = authService.register(request);


        return ResponseEntity.ok(
                ApiResponse.<PhonePasswordRegisterResponse>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(response)
                        .build()
        );
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<PhonePasswordLoginResponse>> login
    (@Valid @RequestBody LoginRequest request) {
        PhonePasswordLoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<PhonePasswordLoginResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .build()
        );
    }

    // ✅ VALIDATE TOKEN
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>>
    validate(@RequestHeader("Authorization") String accessToken) {

        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        boolean valid = authService.validate(accessToken, TokenType.ACCESS_TOKEN);

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
    public ResponseEntity<ApiResponse<RefreshTokenResponse>>
    refresh(@RequestHeader("Authorization") String refreshToken
            ) {

        log.info("refresh token in /refresh: {}", refreshToken);


        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        if (!authService.validate(refreshToken, TokenType.REFRESH_TOKEN)) {
            throw new InvalidOrExpiredTokenException(refreshToken);
        }

        RefreshTokenResponse response = authService.refresh(refreshToken);

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


    @PostMapping("/change-phone")
    public ResponseEntity<ApiResponse<ChangePhoneNumberResponse>> changePhoneNumber(@Valid @RequestBody ChangePhoneNumberRequest request) {

        ChangePhoneNumberResponse response = authService.changePhoneNumber(request);

        return ResponseEntity.ok(
                ApiResponse.<ChangePhoneNumberResponse>builder()
                        .success(true)
                        .message(response.getMessage())
                        .data(response)
                        .build()

        );
    }

    @PostMapping("/change-pass")
    public ResponseEntity<ApiResponse<ChangePasswordWithPhoneResponse>> changePasswordFOrPhone(@Valid @RequestBody ChangePasswordWithPhoneRequest request) {
        ChangePasswordWithPhoneResponse response = authService.changePassword(request);

        return ResponseEntity.ok(
                ApiResponse.<ChangePasswordWithPhoneResponse>builder()
                        .success(true)
                        .message(response.getMessage())
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

    @PostMapping("/forget-password")
    private ResponseEntity<ApiResponse<ForgetPasswordResponse>> forgetPassword
            (@Valid @RequestBody ForgetPasswordRequest request) {
        ForgetPasswordResponse response = authService.forgetPassword(request);

        return ResponseEntity.ok(
                ApiResponse.<ForgetPasswordResponse>builder()
                        .success(true)
                        .message(response.getMessage())
                        .data(response)
                        .errorCode(null)
                        .build()
        );
    }

    @PostMapping("/forget-password-verify-otp")
    private ResponseEntity<ApiResponse<VerifyOtpResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        VerifyOtpResponse response = authService.verifyOtp(verifyOtpRequest);

        return ResponseEntity.ok(
                ApiResponse.<VerifyOtpResponse>builder()
                        .success(true)
                        .message(response.getMessage())
                        .data(response)
                        .errorCode(null)
                        .build()
        );
    }


}
