package com.biharigraphic.jilamart.product.repo;

import com.biharigraphic.jilamart.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.productCategory
        WHERE p.id = :id
    """)
    Optional<Product> findWithCategory(@Param("id") Long id);

    Page<Product> findByActiveTrue(Pageable pageable);

    List<Product> findByFeaturedTrue();

    List<Product> findByProductCategoryId(Long categoryId);
}