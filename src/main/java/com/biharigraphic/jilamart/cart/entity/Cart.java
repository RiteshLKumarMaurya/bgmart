package com.biharigraphic.jilamart.cart.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.entity.CartItem;
import com.biharigraphic.jilamart.product.entity.Product;
import com.biharigraphic.jilamart.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Proper mapping
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems=new ArrayList<>();

    // ✅ Owner side
    @OneToOne
    @JoinColumn(name = "user_id" ,unique = true)
    private User user;

    public void addProduct(Product product, int qty) {

        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Invalid product");
        }

        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        for (CartItem item : cartItems) {
            if (Objects.equals(item.getProduct().getId(), product.getId())) {

                int newQty = item.getQuantity() + qty;

                if (newQty > product.getStock()) {
                    throw new IllegalArgumentException("Out of stock !!");
                }

                item.setQuantity(newQty);
                return;
            }
        }


        Long stock = product.getStock();
        if (stock == null || qty > stock) {
            throw new IllegalArgumentException("Out of stock !!");
        }


        CartItem newItem = new CartItem();
        newItem.setProduct(product);
        newItem.setQuantity(qty);
        newItem.setCart(this);

        cartItems.add(newItem);
    }

    public void removeProduct(Product product) {
        cartItems.removeIf(item -> {
            if (item.getProduct() != null &&
                    Objects.equals(item.getProduct().getId(), product.getId())) {
                item.setCart(null);
                return true;
            }
            return false;
        });
    }


    public BigDecimal getTotalAmount() {
        if (cartItems.isEmpty()) return BigDecimal.ZERO;

        return cartItems.stream()
                .map(item -> {
                    BigDecimal price = item.getProduct().getPrice();
                    if (price == null) price = BigDecimal.ZERO;

                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateQuantity(Product product, int qty) {
        for (CartItem item : cartItems) {
            if (item.getProduct() != null &&
                    Objects.equals(item.getProduct().getId(), product.getId())){

                if (qty <= 0) {
                    removeProduct(product);
                    return;
                }

                Long  stock = product.getStock();
                if (stock == null || qty > stock) {
                    throw new IllegalArgumentException("Out of stock");
                }

                item.setQuantity(qty);
                return;
            }
        }
    }

    public void clearCart() {
        for (CartItem item : cartItems) {
            item.setCart(null);
        }
        cartItems.clear();
    }
}