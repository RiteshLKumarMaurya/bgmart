package com.biharigraphic.jilamart.payments.model.enums;

public enum PaymentStatus {
    PENDING,
    AUTHORIZED,
    CAPTURED,
    COMPLETED, // success final status
    FAILED,
    REFUNDED,
    CANCELLED
}
