package com.biharigraphic.jilamart.productcategory.service.impl;

import com.biharigraphic.jilamart.productcategory.entity.ProductCategory;
import com.biharigraphic.jilamart.productcategory.mapper.ProductCategoryMapper;
import com.biharigraphic.jilamart.productcategory.model.req.CreateCategoryRequest;
import com.biharigraphic.jilamart.productcategory.model.resp.CategoryResponse;
import com.biharigraphic.jilamart.productcategory.repo.ProductCategoryRepository;
import com.biharigraphic.jilamart.productcategory.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository repository;
    private final ProductCategoryMapper mapper;

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {

        if (repository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists");
        }

        ProductCategory category = mapper.toEntity(request);

        return mapper.toResponse(repository.save(category));
    }

    @Override
    public CategoryResponse getById(Long id) {

        ProductCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return mapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllActive() {

        return repository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse update(Long id, CreateCategoryRequest request) {

        ProductCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setShortInfo(request.getShortInfo());
        category.setLongInfo(request.getLongInfo());
        category.setImgUrl(request.getImgUrl());
        category.setIsActive(request.getIsActive());
        category.setDisplayOrder(request.getDisplayOrder());

        return mapper.toResponse(repository.save(category));
    }

    @Override
    public void delete(Long id) {

        ProductCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setIsActive(false); // soft delete
        repository.save(category);
    }
}