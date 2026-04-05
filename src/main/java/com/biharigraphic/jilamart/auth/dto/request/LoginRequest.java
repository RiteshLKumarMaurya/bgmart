
// src/main/java/com/brizerhero/lockeryard/dtos/LoginRequest.java
package com.biharigraphic.jilamart.auth.dto.request;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;

    private String role; // or List<String> roles;

}