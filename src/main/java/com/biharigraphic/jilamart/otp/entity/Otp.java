package com.biharigraphic.jilamart.otp.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class Otp extends BaseEntity {

    private String email;
    private String otp;

    private Instant expiryTime;

    private boolean used = false;
}