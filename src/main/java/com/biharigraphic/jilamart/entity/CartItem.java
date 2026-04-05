package com.biharigraphic.jilamart.entity;

import com.biharigraphic.jilamart.cart.entity.Cart;
import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "cart_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"cart_id", "product_id"})
        },
        indexes = {
                @Index(name = "idx_cart_id", columnList = "cart_id")
        }
)
@Getter
@Setter
@ToString(exclude = "cart")
@EqualsAndHashCode(exclude = "cart")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;


    // ✅ Must have
    @Column(nullable = false)
    private Integer quantity;
}