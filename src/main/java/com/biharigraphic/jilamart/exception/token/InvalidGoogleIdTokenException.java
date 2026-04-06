package com.biharigraphic.jilamart.exception.token;

import com.biharigraphic.jilamart.exception.base.AppException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;

public class InvalidGoogleIdTokenException extends AppException {
    public InvalidGoogleIdTokenException() {
        super("Google token verification failed", ErrorCode.INVALID_GOOGLE_ID_TOKEN.name());
    }
}
