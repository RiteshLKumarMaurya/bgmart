package com.biharigraphic.jilamart.wallet.repo;

import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);
}