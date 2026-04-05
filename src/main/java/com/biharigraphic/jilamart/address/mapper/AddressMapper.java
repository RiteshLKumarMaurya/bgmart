package com.biharigraphic.jilamart.address.mapper;

import com.biharigraphic.jilamart.address.entity.Address;
import com.biharigraphic.jilamart.address.model.res.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
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
                .isDefault(address.getIsDefault())
                .type(address.getType())//HOME, WOK, OTHER
                .build();
    }
}