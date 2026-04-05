package com.biharigraphic.jilamart.address.model.res;

import com.biharigraphic.jilamart.enums.AddressType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {

    private Long id;

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

    private Boolean isDefault;

    private AddressType type;
}