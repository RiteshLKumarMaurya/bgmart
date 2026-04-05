package com.biharigraphic.jilamart.user.mapper.impl;

import com.biharigraphic.jilamart.address.model.res.AddressResponse;
import com.biharigraphic.jilamart.user.dto.response.UserResponse;
import com.biharigraphic.jilamart.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapperImpl {
    public UserResponse mapToUserResponse(User user) {

        List<AddressResponse> addressList = user.getAddresses()
                .stream()
                .map(address -> AddressResponse.builder()
                        .id(address.getId())
                        .fullName(address.getFullName())
                        .phoneNumber(address.getPhoneNumber())
                        .areaName(address.getAreaName())
                        .streetName(address.getStreetName())
                        .city(address.getCity())
                        .stateName(address.getStateName())
                        .zipCode(address.getZipCode())
                        .nearFamousAddress(address.getNearFamousAddress())
                        .latitude(address.getLatitude())
                        .longitude(address.getLongitude())
                        .build()
                ).toList();

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmailId())
                .phone(user.getPhoneNumber())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .addresses(addressList)
                .build();
    }
}
