package com.biharigraphic.jilamart.product.model.reqs;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private String shortInfo;

    private BigDecimal price;
    private BigDecimal discountPrice;

    private String brand;
    private BigDecimal rating;

    private String thumbnail;
    private List<String> images;

    private boolean featured;
}