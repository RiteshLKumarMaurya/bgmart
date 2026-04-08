package com.biharigraphic.jilamart.auth.dto;

import com.biharigraphic.jilamart.auth.dto.response.TokenResponse;
import com.biharigraphic.jilamart.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhonePasswordRegisterResponse {
    private UserResponse userResponse;
    private TokenResponse tokenResponse;
}
