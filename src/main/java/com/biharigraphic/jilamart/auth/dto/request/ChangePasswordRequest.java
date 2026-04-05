package com.biharigraphic.jilamart.auth.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String username;
    private String currentPassword;
    private String newPassword;

    // Getters & Setters
}