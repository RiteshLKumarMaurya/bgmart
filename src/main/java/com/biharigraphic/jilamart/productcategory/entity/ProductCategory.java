package com.biharigraphic.jilamart.productcategory.entity;

import com.biharigraphic.jilamart.entity.BaseEntity;
import com.biharigraphic.jilamart.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 300)
    private String shortInfo;

    @Column(length = 2000)
    private String longInfo;

    private String imgUrl;

    // ✅ Correct reverse mapping
    @OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    private Boolean isActive = true;

    //ui me kis category ko pahle hme dikhana hai wo order
    private Integer displayOrder = 0;


}