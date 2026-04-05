package com.biharigraphic.jilamart.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteAccountResponseDto {
    private boolean success;
    private String message;
}
