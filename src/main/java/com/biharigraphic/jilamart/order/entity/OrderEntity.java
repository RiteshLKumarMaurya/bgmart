package com.biharigraphic.jilamart.order.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.enums.OrderStatus;
import com.biharigraphic.jilamart.entity.OrderItem;
import com.biharigraphic.jilamart.payments.model.entities.Payment;
import com.biharigraphic.jilamart.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "idx_user", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "orderStatus")
})
@Getter
@Setter
public class OrderEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Payment> payments;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;


    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal finalAmount;

    private String address;
    private String phone;

    private Instant orderDate;
    private Instant deliveredDate;


}
