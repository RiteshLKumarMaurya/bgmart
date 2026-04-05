package com.biharigraphic.jilamart.auth.dto.response;

import lombok.Data;

@Data
public class ChangeUsernameResponse {
    private boolean success;
    private String message;

    public ChangeUsernameResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}