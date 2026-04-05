package com.biharigraphic.jilamart.product.service.impl;

import com.biharigraphic.jilamart.product.entity.Product;
import com.biharigraphic.jilamart.product.mapper.ProductMapper;
import com.biharigraphic.jilamart.product.model.req.CreateProductRequest;
import com.biharigraphic.jilamart.product.model.reqs.ProductResponse;
import com.biharigraphic.jilamart.product.repo.ProductRepository;
import com.biharigraphic.jilamart.product.service.ProductService;
import com.biharigraphic.jilamart.productcategory.entity.ProductCategory;
import com.biharigraphic.jilamart.productcategory.repo.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {

        if (productRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("SKU already exists");
        }

        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = mapper.toEntity(request, category);

        return mapper.toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProduct(Long id) {

        Product product = productRepository.findWithCategory(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return productRepository.findByActiveTrue(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public List<ProductResponse> getFeaturedProducts() {

        return productRepository.findByFeaturedTrue()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getByCategory(Long categoryId) {

        return productRepository.findByProductCategoryId(categoryId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(Long id, CreateProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setStock(request.getStock());
        product.setBrand(request.getBrand());

        return mapper.toResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(false); // soft delete
        productRepository.save(product);
    }
}