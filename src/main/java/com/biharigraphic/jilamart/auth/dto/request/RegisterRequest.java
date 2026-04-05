// src/main/java/com/brizerhero/lockeryard/dtos/RegisterRequest.java
package com.biharigraphic.jilamart.auth.dto.request;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String phoneNumber;

    private String roleName; // or List<String> roles;
}



