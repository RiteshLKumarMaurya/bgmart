package com.biharigraphic.jilamart.product.service;

import com.biharigraphic.jilamart.product.model.req.CreateProductRequest;
import com.biharigraphic.jilamart.product.model.reqs.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse getProduct(Long id);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    List<ProductResponse> getFeaturedProducts();

    List<ProductResponse> getByCategory(Long categoryId);

    ProductResponse updateProduct(Long id, CreateProductRequest request);

    void deleteProduct(Long id);
}