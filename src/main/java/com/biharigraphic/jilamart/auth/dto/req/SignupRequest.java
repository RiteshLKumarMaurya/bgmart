package com.biharigraphic.jilamart.auth.dto.req;

import lombok.Data;

@Data
public class SignupRequest {

    private String phoneNumber;
    private String password;
    private String fullName;

    // getters & setters
}