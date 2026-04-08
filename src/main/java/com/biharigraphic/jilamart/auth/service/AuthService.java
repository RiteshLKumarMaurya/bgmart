package com.biharigraphic.jilamart.auth.service;

import com.biharigraphic.jilamart.auth.dto.*;
import com.biharigraphic.jilamart.auth.dto.request.ChangePasswordRequest;
import com.biharigraphic.jilamart.auth.dto.request.ChangeUsernameRequest;
import com.biharigraphic.jilamart.auth.dto.request.LoginRequest;
import com.biharigraphic.jilamart.auth.dto.request.RegisterRequest;
import com.biharigraphic.jilamart.auth.dto.response.ChangePasswordResponse;
import com.biharigraphic.jilamart.auth.dto.response.ChangeUsernameResponse;
import com.biharigraphic.jilamart.auth.dto.response.RefreshTokenResponse;
import com.biharigraphic.jilamart.enums.TokenType;

public interface AuthService {

    PhonePasswordRegisterResponse register(RegisterRequest request);

    PhonePasswordLoginResponse login(LoginRequest request);

    boolean validate(String token, TokenType type);

    RefreshTokenResponse refresh(String refreshToken);

    ChangeUsernameResponse changeUsername(ChangeUsernameRequest request);

    ChangePasswordResponse changePassword(ChangePasswordRequest request);

    ChangePhoneNumberResponse changePhoneNumber(ChangePhoneNumberRequest request);

    ChangePasswordWithPhoneResponse changePassword(ChangePasswordWithPhoneRequest request);

    //FORGET PASSWORD email OTP
    ForgetPasswordResponse forgetPassword(ForgetPasswordRequest request);

    //VERIFY otp in backend
    VerifyOtpResponse verifyOtp(VerifyOtpRequest request);
}