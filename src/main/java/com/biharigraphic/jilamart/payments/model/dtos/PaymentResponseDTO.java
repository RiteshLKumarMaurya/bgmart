package com.biharigraphic.jilamart.payments.model.dtos;

import com.biharigraphic.jilamart.payments.model.enums.PaymentMethodType;
import com.biharigraphic.jilamart.payments.model.enums.PaymentStatus;
import com.biharigraphic.jilamart.payments.model.enums.PaymentType;
import com.biharigraphic.jilamart.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO{
    //if not null then show only
    private Instant paymentTime;
    private BigDecimal amount;
    private PaymentType paymentType;
    private PaymentStatus status;
    private PaymentMethodType paymentMethod;//todo:i think is unused and need to remove from the entity class and all from the is using


    //payment collectors

    //collectedBy.userId

    //receivedBy.receivedBy
    private User payer;


    //to keep track the transaction of the money-> transactionId
    private String razorpayOrderId;

    private String currency;
    ///razorpay
    private String razorpayPaymentId;

    private Boolean captured;
    private String methodDetail; // e.g., "amazonpay"
    private String email;
    private String contact;

    //no need of this
//    private Long globaryId;

    //todo: not needed for now
//    private RazorpayWebhookEventDTO lastRazorpayWebhookEvent;

    private Boolean refundRequired;

    private Boolean refunded;


}
