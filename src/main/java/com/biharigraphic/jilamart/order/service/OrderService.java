package com.biharigraphic.jilamart.order.service;

import com.biharigraphic.jilamart.order.dto.CheckoutRequest;
import com.biharigraphic.jilamart.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse checkout(Long userId, CheckoutRequest request);

    OrderResponse getOrder(Long orderId);

    List<OrderResponse> getUserOrders(Long userId);
}