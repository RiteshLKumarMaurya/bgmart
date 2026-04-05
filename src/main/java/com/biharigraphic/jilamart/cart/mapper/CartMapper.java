package com.biharigraphic.jilamart.cart.mapper;

import com.biharigraphic.jilamart.cart.dto.CartItemResponse;
import com.biharigraphic.jilamart.cart.dto.CartResponse;
import com.biharigraphic.jilamart.cart.entity.Cart;
import com.biharigraphic.jilamart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart) {

        CartResponse response = new CartResponse();

        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(this::toItemResponse)
                .toList();

        response.setItems(items);
        response.setTotalAmount(cart.getTotalAmount());

        return response;
    }

    private CartItemResponse toItemResponse(CartItem item) {

        CartItemResponse res = new CartItemResponse();

        res.setProductId(item.getProduct().getId());
        res.setProductName(item.getProduct().getName());
        res.setPrice(item.getProduct().getPrice());
        res.setQuantity(item.getQuantity());

        return res;
    }
}