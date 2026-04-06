package com.biharigraphic.jilamart.exception;

import com.biharigraphic.jilamart.exception.base.AppException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;

public class InvalidOrExpiredTokenException extends AppException {
    public InvalidOrExpiredTokenException() {
        super("invalid or expired token found", ErrorCode.INVALID_OR_EXPIRED_TOKEN.name());
    }

    public InvalidOrExpiredTokenException(String token){
        super("invalid or expired token :" + token, ErrorCode.INVALID_OR_EXPIRED_TOKEN.name());

    }
}
