package com.biharigraphic.jilamart.wallet.service;

import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.wallet.dto.TransactionResponse;
import com.biharigraphic.jilamart.wallet.dto.WalletResponse;
import com.biharigraphic.jilamart.wallet.entity.CoinTransaction;
import com.biharigraphic.jilamart.wallet.entity.Wallet;
import com.biharigraphic.jilamart.wallet.enums.TransactionType;
import com.biharigraphic.jilamart.wallet.repo.CoinTransactionRepository;
import com.biharigraphic.jilamart.wallet.repo.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final CoinTransactionRepository transactionRepository;

    @Transactional
    public void addCoins(User user, Long amount, String desc) {

        Wallet wallet = walletRepository.findByUser(user)
                .orElseGet(() -> {
                    Wallet w = new Wallet();
                    w.setUser(user);
                    return w;
                });

        wallet.setBalance(wallet.getBalance() + amount);

        CoinTransaction trx = new CoinTransaction();
        trx.setUser(user);
        trx.setAmount(amount);
        trx.setType(TransactionType.CREDIT);
        trx.setDescription(desc);

        walletRepository.save(wallet);
        transactionRepository.save(trx);
    }

    @Transactional
    public void useCoins(User user, Long amount) {

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance() - amount);

        CoinTransaction trx = new CoinTransaction();
        trx.setUser(user);
        trx.setAmount(amount);
        trx.setType(TransactionType.DEBIT);
        trx.setDescription("Used in order");

        walletRepository.save(wallet);
        transactionRepository.save(trx);
    }

    public WalletResponse getWallet(User user) {
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return new WalletResponse(wallet.getBalance());
    }

    public List<TransactionResponse> getTransactions(User user) {

        return transactionRepository.findByUser(user)
                .stream()
                .map(t -> new TransactionResponse(
                        t.getAmount(),
                        t.getType().name(),
                        t.getDescription()
                ))
                .toList();
    }
}