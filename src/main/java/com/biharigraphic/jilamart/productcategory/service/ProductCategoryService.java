package com.biharigraphic.jilamart.productcategory.service;

import com.biharigraphic.jilamart.productcategory.model.req.CreateCategoryRequest;
import com.biharigraphic.jilamart.productcategory.model.resp.CategoryResponse;

import java.util.List;

public interface ProductCategoryService {

    CategoryResponse create(CreateCategoryRequest request);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAllActive();

    CategoryResponse update(Long id, CreateCategoryRequest request);

    void delete(Long id);
}