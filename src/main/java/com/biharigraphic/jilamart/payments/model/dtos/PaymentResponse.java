package com.biharigraphic.jilamart.payments.model.dtos;

import com.biharigraphic.jilamart.payments.model.enums.PaymentManType;
import com.biharigraphic.jilamart.payments.model.enums.PaymentMethodType;
import com.biharigraphic.jilamart.payments.model.enums.PaymentStatus;
import com.biharigraphic.jilamart.payments.model.enums.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
// for offline <-> COD
public class PaymentResponse {

    private BigDecimal amount;
    private PaymentType paymentType;
    private PaymentStatus status;

    // UTC timestamps
    private Instant paymentTime;
    private String receiptNumber;
    private Instant collectedAt;

    private PaymentMethodType paymentMethod;
    private PaymentManType paymentMan;
}
