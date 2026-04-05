package com.biharigraphic.jilamart.wallet.dto;

import lombok.Data;

@Data
public class TransactionResponse {

    private Long amount;
    private String type;
    private String description;

    public TransactionResponse(Long amount, String type, String description) {
        this.amount = amount;
        this.type = type;
        this.description = description;
    }
}