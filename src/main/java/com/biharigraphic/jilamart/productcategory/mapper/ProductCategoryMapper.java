package com.biharigraphic.jilamart.productcategory.mapper;

import com.biharigraphic.jilamart.productcategory.entity.ProductCategory;
import com.biharigraphic.jilamart.productcategory.model.req.CreateCategoryRequest;
import com.biharigraphic.jilamart.productcategory.model.resp.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryMapper {

    public ProductCategory toEntity(CreateCategoryRequest req) {

        ProductCategory category = new ProductCategory();

        category.setName(req.getName());
        category.setShortInfo(req.getShortInfo());
        category.setLongInfo(req.getLongInfo());
        category.setImgUrl(req.getImgUrl());
        category.setIsActive(req.getIsActive() != null ? req.getIsActive() : true);
        category.setDisplayOrder(req.getDisplayOrder());

        return category;
    }

    public CategoryResponse toResponse(ProductCategory category) {

        CategoryResponse res = new CategoryResponse();

        res.setId(category.getId());
        res.setName(category.getName());
        res.setShortInfo(category.getShortInfo());
        res.setImgUrl(category.getImgUrl());
        res.setIsActive(category.getIsActive());
        res.setDisplayOrder(category.getDisplayOrder());

        return res;
    }
}