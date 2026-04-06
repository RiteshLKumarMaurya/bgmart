package com.biharigraphic.jilamart.exception.password;

import com.biharigraphic.jilamart.exception.base.AppException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;

public class WrongPasswordEnteredException extends AppException {

    public WrongPasswordEnteredException(String password) {
        super("wrong password entered : "+password, ErrorCode.WRONG_PASSWORD_ENTERED.name());
    }
}
