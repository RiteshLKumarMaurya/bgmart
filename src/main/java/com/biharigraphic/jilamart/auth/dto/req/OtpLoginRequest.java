package com.biharigraphic.jilamart.auth.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpLoginRequest {
    private String firebaseToken;
}