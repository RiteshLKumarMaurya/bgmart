package com.biharigraphic.jilamart.user.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DeleteAccountRequest extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // better control of FK column name
    private User user;

    @Column(length = 50) // limit to avoid TEXT unless needed
    private String reasonType;

    @Column(columnDefinition = "TEXT") // limit so PostgreSQL stores it as varchar instead of TEXT
    private String reasonDescription;

    @Column(length = 15) // phone number max length
    private String phone;
}
