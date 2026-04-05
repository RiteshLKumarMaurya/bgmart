package com.biharigraphic.jilamart.cart.service.impl;

import com.biharigraphic.jilamart.cart.dto.AddToCartRequest;
import com.biharigraphic.jilamart.cart.dto.CartResponse;
import com.biharigraphic.jilamart.cart.mapper.CartMapper;
import com.biharigraphic.jilamart.cart.repo.CartRepository;
import com.biharigraphic.jilamart.cart.service.CartService;
import com.biharigraphic.jilamart.cart.entity.Cart;
import com.biharigraphic.jilamart.product.entity.Product;
import com.biharigraphic.jilamart.product.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    public void addToCart(Long userId, AddToCartRequest request) {

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseGet(() -> createCart(userId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cart.addProduct(product, request.getQuantity());

        cartRepository.save(cart);
    }

    @Override
    public CartResponse getCart(Long userId) {

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cartMapper.toResponse(cart);
    }

    @Override
    public void removeItem(Long userId, Long productId) {

        Cart cart = getCartEntity(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow();

        cart.removeProduct(product);

        cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long userId) {

        Cart cart = getCartEntity(userId);

        cart.clearCart();

        cartRepository.save(cart);
    }

    private Cart getCartEntity(Long userId) {
        return cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    private Cart createCart(Long userId) {
        Cart cart = new Cart();
        // set user
        return cartRepository.save(cart);
    }
}