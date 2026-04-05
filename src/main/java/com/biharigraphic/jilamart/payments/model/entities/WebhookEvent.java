package com.biharigraphic.jilamart.payments.model.entities;


import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.payments.model.enums.PaymentEventType;
import com.biharigraphic.jilamart.payments.model.enums.WebhookProcessingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "webhook_events")
public class WebhookEvent extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private PaymentEventType eventType;

    @Enumerated(EnumType.STRING)
    private WebhookProcessingStatus processingStatus;

    private String razorpayEventId;

    @Column(columnDefinition = "TEXT")
    private String payloadJson;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private Instant receivedAt; ///now no need already have the createdAt
}
