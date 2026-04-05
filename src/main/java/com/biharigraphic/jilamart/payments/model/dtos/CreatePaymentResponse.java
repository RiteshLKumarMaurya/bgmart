package com.biharigraphic.jilamart.payments.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentResponse {
    //the order id of the razorpay to checkout the payment to make it success

    private String id;
    private String razorpayKeyId;
    //not need of these fields
//    private String currency;
//    private BigDecimal amount;
}
