package com.biharigraphic.jilamart.payments.model.dtos;


import com.biharigraphic.jilamart.payments.model.enums.PaymentMethodType;
import com.biharigraphic.jilamart.payments.model.enums.PaymentStatus;
import com.biharigraphic.jilamart.payments.model.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private BigDecimal amount;//in paise
    private PaymentType paymentType;//card,wallet,upi etc
    private PaymentStatus status;//default PENDING
    private PaymentMethodType paymentMethod;
    private String currency;
}
