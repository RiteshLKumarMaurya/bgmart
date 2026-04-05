package com.biharigraphic.jilamart.productcategory.model.req;

import lombok.Data;

@Data
public class CreateCategoryRequest {

    private String name;
    private String shortInfo;
    private String longInfo;
    private String imgUrl;
    private Boolean isActive;
    private Integer displayOrder;
}