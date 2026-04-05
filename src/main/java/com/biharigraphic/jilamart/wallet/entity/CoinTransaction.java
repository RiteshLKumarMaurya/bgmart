package com.biharigraphic.jilamart.wallet.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.wallet.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CoinTransaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String description;
}