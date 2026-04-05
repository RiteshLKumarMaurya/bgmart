package com.biharigraphic.jilamart.cart.repo;

import com.biharigraphic.jilamart.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser_Id(Long userId);

    @Query("""
        SELECT c FROM Cart c
        LEFT JOIN FETCH c.cartItems ci
        LEFT JOIN FETCH ci.product
        WHERE c.user.id = :userId
    """)
    Optional<Cart> findCartWithItems(@Param("userId") Long userId);

    void deleteByUserId(Long userId);

    boolean existsByUserId(Long userId);
}