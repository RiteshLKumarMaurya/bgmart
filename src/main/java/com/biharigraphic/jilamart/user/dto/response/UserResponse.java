package com.biharigraphic.jilamart.user.dto.response;

import com.biharigraphic.jilamart.address.model.res.AddressResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private String profilePictureUrl;

    private List<AddressResponse> addresses; // 🔥 important
}