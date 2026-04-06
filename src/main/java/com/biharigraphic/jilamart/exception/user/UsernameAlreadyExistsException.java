package com.biharigraphic.jilamart.exception.user;

import com.biharigraphic.jilamart.exception.base.AppException;

public class UsernameAlreadyExistsException extends AppException {
    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username, "USERNAME_EXISTS");
    }
}