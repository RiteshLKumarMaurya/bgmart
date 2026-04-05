package com.biharigraphic.jilamart.payments.model.requests;

public record CashHandoverRequest(
    Long paymentId,
    Long managerId
) {}