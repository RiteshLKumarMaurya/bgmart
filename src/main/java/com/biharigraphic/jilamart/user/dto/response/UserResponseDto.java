package com.biharigraphic.jilamart.user.dto.response;

import com.biharigraphic.jilamart.address.model.res.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String fullName;
    private String phone;
    private String profilePictureUrl;
    private String email;

    private String username;
    private String roleName;

    private List<AddressResponse> addresses;


    private Instant createdAt;


}
