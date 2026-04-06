package com.biharigraphic.jilamart.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpLoginRequest {

    @NotBlank(message = "Firebase token is required")
    @Size(min = 10, message = "Invalid firebase token")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Invalid firebase token format")
    private String firebaseToken;
}