package com.biharigraphic.jilamart.order.controller;

import com.biharigraphic.jilamart.order.dto.CheckoutRequest;
import com.biharigraphic.jilamart.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @RequestHeader("userId") Long userId,
            @RequestBody CheckoutRequest request
    ) {
        return ResponseEntity.ok(orderService.checkout(userId, request));
    }
}