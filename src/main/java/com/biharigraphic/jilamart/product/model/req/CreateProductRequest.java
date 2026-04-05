package com.biharigraphic.jilamart.product.model.req;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProductRequest {

    private String name;
    private String shortInfo;
    private String longInfo;
    private Long stock;
    private Float weight;

    private BigDecimal price;
    private BigDecimal discountPrice;

    private String sku;
    private String brand;

    private Boolean fragile;
    private Long categoryId;

    private String thumbnail;
    private List<String> imgUrls;
}