package com.biharigraphic.jilamart.user.exception;

import com.biharigraphic.jilamart.exception.base.AppException;

public class UserException extends AppException {
    public UserException(String message, String errorCode) {
        super(message, errorCode);
    }
}
