package com.biharigraphic.jilamart.payments.model.dtos;

import com.biharigraphic.jilamart.payments.model.enums.PaymentEventType;
import com.biharigraphic.jilamart.payments.model.enums.WebhookProcessingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RazorpayWebhookEventDTO {

    private PaymentEventType eventType;

    private WebhookProcessingStatus processingStatus;

    private Instant receivedAt;

}
