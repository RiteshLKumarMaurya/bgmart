package com.biharigraphic.jilamart.exception.user;

import com.biharigraphic.jilamart.exception.base.AppException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;

public class UserNotFoundForUsernameException extends AppException {

    public UserNotFoundForUsernameException(String message, String errorCode) {
        super(message, errorCode);
    }

    public UserNotFoundForUsernameException(String username){
        super("user not found for username: "+username, ErrorCode.USER_NOT_FOUND_FOR_USERNAME.name());
    }
}
