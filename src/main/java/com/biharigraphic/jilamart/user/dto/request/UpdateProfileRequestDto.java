package com.biharigraphic.jilamart.user.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProfileRequestDto {
    private String fullName;
    private String phoneNumber;
    private String emailId;

    // Constructor, getters, setters
}
