package com.biharigraphic.jilamart.wallet.repo;

import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.wallet.entity.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinTransactionRepository extends JpaRepository<CoinTransaction, Long> {
    List<CoinTransaction> findByUser(User user);
}