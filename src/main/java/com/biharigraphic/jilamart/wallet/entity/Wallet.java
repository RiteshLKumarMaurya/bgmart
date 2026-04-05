package com.biharigraphic.jilamart.wallet.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Wallet extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private Long balance = 0L;
}