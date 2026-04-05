package com.biharigraphic.jilamart.product_feedback.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.product.entity.Product;
import com.biharigraphic.jilamart.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(
        name = "product_feedback",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "product_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// ProductFeedback.java
public class ProductFeedback extends BaseEntity {

    // 👤 कौन user ने दिया
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 📦 किस product पर
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // ⭐ Rating (1–5)
    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Float ratingValue;

    // 💬 Review
    @Column(length = 1000)
    private String ratingMessage;



}