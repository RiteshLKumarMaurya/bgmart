package com.biharigraphic.jilamart.address.service;

import com.biharigraphic.jilamart.address.model.req.AddressRequest;
import com.biharigraphic.jilamart.address.model.res.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(String token, AddressRequest request);

    List<AddressResponse> getUserAddresses(String token);

    AddressResponse updateAddress(Long id, String token, AddressRequest request);

    void deleteAddress(Long id, String token);
}