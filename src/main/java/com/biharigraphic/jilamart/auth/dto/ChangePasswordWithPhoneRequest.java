package com.biharigraphic.jilamart.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordWithPhoneRequest {

    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "phone number must be 10 digits")
    @Size(min = 10,max = 10)
    private String phoneNumber;

    @NotBlank(message = "password is required")
    private String currentPassword;

    @NotBlank(message = "new password is required")
    private String newPassword;

}
