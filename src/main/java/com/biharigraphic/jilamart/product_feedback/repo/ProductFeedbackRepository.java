package com.biharigraphic.jilamart.product_feedback.repo;

import com.biharigraphic.jilamart.product_feedback.entity.ProductFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductFeedbackRepository extends JpaRepository<ProductFeedback, Long> {

    Optional<ProductFeedback> findByUserIdAndProductId(Long userId, Long productId);

    List<ProductFeedback> findByProductId(Long productId);
}