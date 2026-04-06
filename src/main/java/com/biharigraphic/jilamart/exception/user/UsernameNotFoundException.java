package com.biharigraphic.jilamart.exception.user;

import com.biharigraphic.jilamart.exception.base.AppException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;

public class UsernameNotFoundException extends AppException {

    public UsernameNotFoundException() {
        super("Username not found", ErrorCode.USERNAME_NOT_FOUND.name());
    }

    public UsernameNotFoundException(String username) {
        super("Username not found: " + username, ErrorCode.USERNAME_NOT_FOUND.name());
    }
}