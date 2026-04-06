package com.biharigraphic.jilamart.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangeUsernameRequest {

    @NotBlank(message = "Current username is required")
    private String currentUsername;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "New username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscore")
    private String newUsername;
}