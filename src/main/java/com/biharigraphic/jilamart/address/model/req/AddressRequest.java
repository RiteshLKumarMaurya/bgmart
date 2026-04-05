package com.biharigraphic.jilamart.address.model.req;

import com.biharigraphic.jilamart.enums.AddressType;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AddressRequest {

    private String fullName;
    private String phoneNumber;

    private String areaName;
    private String streetName;
    private String city;
    private String stateName;
    private String zipCode;

    private String nearFamousAddress;

    // 🔥 NEW
    private Double latitude;
    private Double longitude;

    private Boolean isDefault; // optional

    private AddressType type;
}