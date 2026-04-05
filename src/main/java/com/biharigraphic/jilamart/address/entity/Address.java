package com.biharigraphic.jilamart.address.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.enums.AddressType;
import com.biharigraphic.jilamart.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address extends BaseEntity {

    private String fullName;
    private String phoneNumber;

    private String houseNumber;
    private String areaName;
    private String streetName;

    private String city;
    private String stateName;
    private String zipCode;

    private String nearFamousAddress;

    private Double latitude;
    private Double longitude;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Enumerated(EnumType.STRING)
    private AddressType type;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}