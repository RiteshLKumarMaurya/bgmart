package com.biharigraphic.jilamart.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePhoneNumberResponse {
    private String phoneNumber;
    private String message;
}
