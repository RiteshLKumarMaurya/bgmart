package com.biharigraphic.jilamart.auth.dto.request;

import lombok.Data;

@Data
public class ChangeUsernameRequest {
    private String currentUsername;
    private String password;
    private String newUsername;

    // Getters & Setters
}