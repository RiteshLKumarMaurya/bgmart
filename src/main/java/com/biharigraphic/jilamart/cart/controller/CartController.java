package com.biharigraphic.jilamart.cart.controller;

import com.biharigraphic.jilamart.cart.dto.AddToCartRequest;
import com.biharigraphic.jilamart.cart.dto.CartResponse;
import com.biharigraphic.jilamart.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartRequest request,
            @RequestHeader("userId") Long userId
    ) {
        cartService.addToCart(userId, request);
        return ResponseEntity.ok("Added to cart");
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader("userId") Long userId
    ) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeItem(
            @RequestHeader("userId") Long userId,
            @PathVariable Long productId
    ) {
        cartService.removeItem(userId, productId);
        return ResponseEntity.ok("Removed");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(
            @RequestHeader("userId") Long userId
    ) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }
}