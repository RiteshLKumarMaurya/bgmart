package com.biharigraphic.jilamart.exception.otp;

import com.biharigraphic.jilamart.exception.base.AppException;

public class OtpException extends AppException {
    public OtpException(String message, String errorCode) {
        super(message, errorCode);
    }
}
