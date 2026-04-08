package com.biharigraphic.jilamart.exception.user;

import com.biharigraphic.jilamart.exception.base.AppException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;

public class UserPhoneAlreadyRegistered extends AppException {

    // default message
    public UserPhoneAlreadyRegistered() {
        super("Phone number already registered", ErrorCode.PHONE_REGISTERED.name());
    }

    // custom message (optional)
    public UserPhoneAlreadyRegistered(String message) {
        super(message, ErrorCode.PHONE_REGISTERED.name());
    }
}