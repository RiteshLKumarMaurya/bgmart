package com.biharigraphic.jilamart.user.dto.response;

import com.biharigraphic.jilamart.address.model.res.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String fullname;
    private String phone;
    private String email;
    private String roleName;
    private String profilePictureUrl;

    private List<AddressResponse> addresses;

}
