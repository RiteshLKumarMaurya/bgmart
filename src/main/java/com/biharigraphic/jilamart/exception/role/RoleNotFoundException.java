package com.biharigraphic.jilamart.exception.role;

import com.biharigraphic.jilamart.exception.base.AppException;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;

public class RoleNotFoundException extends AppException {
    public RoleNotFoundException(String role,String username) {
        super("Role not found: " + role +"for username: "+username, ErrorCode.ROLE_NOT_FOUND.name());
    }
    public RoleNotFoundException(String role){
        super("Role not found: "+ role,ErrorCode.ROLE_NOT_FOUND.name());
    }
}