package com.biharigraphic.jilamart.productcategory.model.resp;

import lombok.Data;

@Data
public class CategoryResponse {

    private Long id;
    private String name;
    private String shortInfo;
    private String imgUrl;
    private Boolean isActive;
    private Integer displayOrder;
}