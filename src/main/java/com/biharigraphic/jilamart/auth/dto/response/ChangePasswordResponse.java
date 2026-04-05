package com.biharigraphic.jilamart.auth.dto.response;

import lombok.Data;

@Data
public class ChangePasswordResponse {
    private boolean success;
    private String message;

    public ChangePasswordResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters & Setters
}