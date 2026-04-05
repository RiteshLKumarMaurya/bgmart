package com.biharigraphic.jilamart.order.dto;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String address;
    private String phone;
}