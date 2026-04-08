package com.biharigraphic.jilamart.exception;

import com.biharigraphic.jilamart.exception.base.AppException;

public class PhoneNumberNotFoundException extends AppException {
    public PhoneNumberNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
