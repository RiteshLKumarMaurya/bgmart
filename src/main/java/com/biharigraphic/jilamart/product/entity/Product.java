package com.biharigraphic.jilamart.product.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.productcategory.entity.ProductCategory;
import com.biharigraphic.jilamart.product_feedback.entity.ProductFeedback;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Product.java
@ToString(exclude = {"feedbacks", "productCategory"})
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 300)
    private String shortInfo;

    @Column(length = 2000)
    private String longInfo;

    @Column(nullable = false)
    private Long stock;

    private Float weight;

    // 💰 Pricing
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountPrice;

    // 📦 SKU
    @Column(unique = true, nullable = false)
    private String sku;

    // 🏷️ Brand
    @Column(nullable = false)
    private String brand;

    // ⭐ Aggregated rating (FAST READ)
    @Column(precision = 2, scale = 1)
    private BigDecimal rating = BigDecimal.ZERO;

    private Integer ratingCount = 0;



    private Boolean fragile;

    // 🔗 Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory productCategory;

    // 🖼️ Images
    @Column(name = "thumbnail")
    private String thumbnail;

    @ElementCollection
    @CollectionTable(
            name = "product_images",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "image_url")
    private List<String> imgUrls = new ArrayList<>();

    private boolean active = true;
    private boolean featured = false;
    private Integer soldCount = 0;

    // 🔥 Feedback mapping
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductFeedback> feedbacks = new ArrayList<>();
}