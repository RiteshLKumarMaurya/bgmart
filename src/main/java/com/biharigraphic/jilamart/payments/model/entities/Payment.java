package com.biharigraphic.jilamart.payments.model.entities;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.order.entity.OrderEntity;
import com.biharigraphic.jilamart.payments.model.enums.PaymentMethodType;
import com.biharigraphic.jilamart.payments.model.enums.PaymentStatus;
import com.biharigraphic.jilamart.payments.model.enums.PaymentType;
import com.biharigraphic.jilamart.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.core.Ordered;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String paymentStatusPayload;

    // Payment completion time (stored in UTC)
    @Column
    private Instant paymentTime;

    @Column(nullable = false, precision = 10, scale = 2)
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private PaymentStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethod;

    @ManyToOne
    @JoinColumn(name = "payer")
    @JsonBackReference
    private User payer;


    private String currency;
    private String paymentId;

    private String razorpayOrderId;
    private Boolean captured;
    private String methodDetail;
    private String email;
    private String contact;

    @JsonBackReference
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WebhookEvent> webhookEvents = new ArrayList<>();

    @Column(name = "refund_required", nullable = true)
    private Boolean refundRequired;

    @Column(name = "last_processed_event")
    private String lastProcessedEvent;

    @Column(nullable = true)
    private Boolean refunded;


    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
