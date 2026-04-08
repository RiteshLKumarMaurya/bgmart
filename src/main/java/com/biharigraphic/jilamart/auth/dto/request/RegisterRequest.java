package com.biharigraphic.jilamart.auth.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Size(max = 10,message = "Password must be at most 10 characters")
    private String password;

    @NotBlank(message = "fullName is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email contains invalid characters"
    )
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String emailId;

    private String fcmToken;
    private String device;

}