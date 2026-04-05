package com.biharigraphic.jilamart.address.controller;

import com.biharigraphic.jilamart.address.model.req.AddressRequest;
import com.biharigraphic.jilamart.address.model.res.AddressResponse;
import com.biharigraphic.jilamart.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    private String extractToken(String header) {
        return header.replace("Bearer ", "");
    }

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            @RequestHeader("Authorization") String auth,
            @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
                addressService.addAddress(extractToken(auth), request)
        );
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses(
            @RequestHeader("Authorization") String auth) {

        return ResponseEntity.ok(
                addressService.getUserAddresses(extractToken(auth))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth,
            @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
                addressService.updateAddress(id, extractToken(auth), request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {

        addressService.deleteAddress(id, extractToken(auth));
        return ResponseEntity.ok("Address deleted successfully");
    }
}