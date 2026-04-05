package com.biharigraphic.jilamart.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;

    private String device;

    // getters setters
}