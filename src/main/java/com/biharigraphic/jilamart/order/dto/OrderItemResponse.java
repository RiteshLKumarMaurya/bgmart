package com.biharigraphic.jilamart.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private String productName;
    private Integer quantity;
    private BigDecimal price;
}