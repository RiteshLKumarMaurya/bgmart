package com.biharigraphic.jilamart.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteAccountRequestDto {

    private String username;
    private String password;

    private String reasonType;
    private String reasonDescription;
    private String phone;
}
