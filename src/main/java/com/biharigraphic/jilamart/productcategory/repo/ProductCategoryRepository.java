package com.biharigraphic.jilamart.productcategory.repo;

import com.biharigraphic.jilamart.productcategory.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByName(String name);

    boolean existsByName(String name);

    List<ProductCategory> findByIsActiveTrueOrderByDisplayOrderAsc();

}