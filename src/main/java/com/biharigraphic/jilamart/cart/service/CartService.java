package com.biharigraphic.jilamart.cart.service;

import com.biharigraphic.jilamart.cart.dto.AddToCartRequest;
import com.biharigraphic.jilamart.cart.dto.CartResponse;

public interface CartService {

    void addToCart(Long userId, AddToCartRequest request);

    CartResponse getCart(Long userId);

    void removeItem(Long userId, Long productId);

    void clearCart(Long userId);
}