package com.biharigraphic.jilamart.wallet.dto;

import lombok.Data;

@Data
public class WalletResponse {

    private Long balance;

    public WalletResponse(Long balance) {
        this.balance = balance;
    }

    // getter
}