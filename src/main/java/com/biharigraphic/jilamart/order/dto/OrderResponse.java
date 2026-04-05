package com.biharigraphic.jilamart.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponse {

    private Long orderId;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal finalAmount;
    private List<OrderItemResponse> items;
}