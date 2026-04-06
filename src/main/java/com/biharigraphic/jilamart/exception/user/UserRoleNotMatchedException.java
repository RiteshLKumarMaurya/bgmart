package com.biharigraphic.jilamart.exception.user;

import com.biharigraphic.jilamart.exception.base.AppException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;

public class UserRoleNotMatchedException extends AppException {
    public UserRoleNotMatchedException(String roleName,String username) {
        super("role :"+roleName+" not matched for user : "+username, ErrorCode.USER_ROLE_NOT_MATCHED.name());
    }
}
