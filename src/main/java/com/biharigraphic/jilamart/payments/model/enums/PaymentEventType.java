package com.biharigraphic.jilamart.payments.model.enums;

public enum PaymentEventType {
    PAYMENT_CREATED,
    PAYMENT_AUTHORIZED,
    PAYMENT_CAPTURED,
    PAYMENT_FAILED,
    PAYMENT_REFUNDED,
    ORDER_CREATED,
    ORDER_PAID,                 // ✅ Add this
    PAYMENT_DISPUTE_CREATED,    // ✅ Add this
    PAYMENT_DISPUTE_CLOSED,     // ✅ Add this
    REFUND_CREATED,
    REFUND_PROCESSED,
    REFUND_FAILED,              // ✅ Add this
    UNKNOWN
}
