package com.biharigraphic.jilamart.wallet.controller;

import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.wallet.dto.TransactionResponse;
import com.biharigraphic.jilamart.wallet.dto.WalletResponse;
import com.biharigraphic.jilamart.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    // 🔹 Get wallet balance
    @GetMapping
    public WalletResponse getWallet(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return walletService.getWallet(user);
    }

    // 🔹 Get transaction history
    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return walletService.getTransactions(user);
    }
}