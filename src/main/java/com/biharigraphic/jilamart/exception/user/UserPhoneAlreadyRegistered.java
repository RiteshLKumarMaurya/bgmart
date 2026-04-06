package com.biharigraphic.jilamart.exception.user;

import com.biharigraphic.jilamart.exception.base.AppException;

public class UserPhoneAlreadyRegistered extends AppException {

    // default message
    public UserPhoneAlreadyRegistered() {
        super("Phone number already registered", "PHONE_REGISTERED");
    }

    // custom message (optional)
    public UserPhoneAlreadyRegistered(String message) {
        super(message, "PHONE_REGISTERED");
    }
}