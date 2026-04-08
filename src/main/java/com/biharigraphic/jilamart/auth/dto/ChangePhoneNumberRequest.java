package com.biharigraphic.jilamart.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePhoneNumberRequest {

    @NotBlank(message = "current phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "current phone number must be 10 digits")
    @Size(min = 10,max = 10)
    private String currentPhoneNumber;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "new phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "new phone number must be 10 digits")
    @Size(min = 10,max = 10)
    private String newPhoneNumber;
}
