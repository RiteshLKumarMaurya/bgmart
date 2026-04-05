package com.biharigraphic.jilamart.address.service.impl;

import com.biharigraphic.jilamart.address.entity.Address;
import com.biharigraphic.jilamart.address.mapper.AddressMapper;
import com.biharigraphic.jilamart.address.model.req.AddressRequest;
import com.biharigraphic.jilamart.address.model.res.AddressResponse;
import com.biharigraphic.jilamart.address.repo.AddressRepository;
import com.biharigraphic.jilamart.address.service.AddressService;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.utils.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserUtil userUtil;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponse addAddress(String token, AddressRequest request) {

        User user = userUtil.getUserFromTheJwt(token);

        Address address = new Address();
        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAreaName(request.getAreaName());
        address.setStreetName(request.getStreetName());
        address.setCity(request.getCity());
        address.setStateName(request.getStateName());
        address.setZipCode(request.getZipCode());
        address.setNearFamousAddress(request.getNearFamousAddress());

        if (request.getLatitude() != null) address.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) address.setLongitude(request.getLongitude());

        address.setUser(user);

        /// do the false if adding wala address is as default coming

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            // 🔥 Remove old default
            List<Address> addresses = addressRepository.findByUserId(user.getId());
            for (Address addr : addresses) {
                addr.setIsDefault(false);
            }
        }

        address.setIsDefault(Boolean.TRUE.equals(request.getIsDefault()));

        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    public List<AddressResponse> getUserAddresses(String token) {

        User user = userUtil.getUserFromTheJwt(token);

        return addressRepository.findByUserId(user.getId())
                .stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Override
    public AddressResponse updateAddress(Long id, String token, AddressRequest request) {

        User user = userUtil.getUserFromTheJwt(token);

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // 🔐 Ownership check
        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (request.getFullName() != null) address.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) address.setPhoneNumber(request.getPhoneNumber());
        if (request.getAreaName() != null) address.setAreaName(request.getAreaName());
        if (request.getStreetName() != null) address.setStreetName(request.getStreetName());
        if (request.getCity() != null) address.setCity(request.getCity());
        if (request.getStateName() != null) address.setStateName(request.getStateName());
        if (request.getZipCode() != null) address.setZipCode(request.getZipCode());
        if (request.getNearFamousAddress() != null) address.setNearFamousAddress(request.getNearFamousAddress());

        if (request.getLatitude() != null) address.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) address.setLongitude(request.getLongitude());

        //default handling at time of update
        if (request.getIsDefault() != null && request.getIsDefault()) {

            List<Address> addresses = addressRepository.findByUserId(user.getId());
            for (Address addr : addresses) {
                addr.setIsDefault(false);
            }

            address.setIsDefault(true);
        }

        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long id, String token) {

        User user = userUtil.getUserFromTheJwt(token);

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // 🔐 Ownership check
        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        addressRepository.delete(address);
    }
}