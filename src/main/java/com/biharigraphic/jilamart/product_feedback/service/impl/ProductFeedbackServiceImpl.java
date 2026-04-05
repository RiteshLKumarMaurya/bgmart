package com.biharigraphic.jilamart.product_feedback.service.impl;

import com.biharigraphic.jilamart.product.entity.Product;
import com.biharigraphic.jilamart.product.repo.ProductRepository;
import com.biharigraphic.jilamart.product_feedback.entity.ProductFeedback;
import com.biharigraphic.jilamart.product_feedback.mapper.ProductFeedbackMapper;
import com.biharigraphic.jilamart.product_feedback.model.req.CreateFeedbackRequest;
import com.biharigraphic.jilamart.product_feedback.model.res.FeedbackResponse;
import com.biharigraphic.jilamart.product_feedback.repo.ProductFeedbackRepository;
import com.biharigraphic.jilamart.product_feedback.service.ProductFeedbackService;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductFeedbackServiceImpl implements ProductFeedbackService {

    private final ProductFeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductFeedbackMapper mapper;

    @Override
    @Transactional
    public FeedbackResponse addOrUpdateFeedback(Long userId, CreateFeedbackRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<ProductFeedback> existing =
                feedbackRepository.findByUserIdAndProductId(userId, request.getProductId());

        ProductFeedback feedback;

        if (existing.isPresent()) {
            // 🔁 UPDATE
            feedback = existing.get();

            float oldRating = feedback.getRatingValue();
            float newRating = request.getRatingValue();

            feedback.setRatingValue(newRating);
            feedback.setRatingMessage(request.getRatingMessage());

            updateProductRating(product, oldRating, newRating);

        } else {
            // ➕ NEW
            feedback = mapper.toEntity(request, user, product);

            feedbackRepository.save(feedback);

            addNewRating(product, request.getRatingValue());
        }

        return mapper.toResponse(feedback);
    }

    @Override
    public List<FeedbackResponse> getProductFeedbacks(Long productId) {

        return feedbackRepository.findByProductId(productId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteFeedback(Long userId, Long productId) {

        ProductFeedback feedback = feedbackRepository
                .findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        Product product = feedback.getProduct();

        removeRating(product, feedback.getRatingValue());

        feedbackRepository.delete(feedback);
    }

    // 🔥 RATING LOGIC

    private void addNewRating(Product product, float newRating) {

        int count = product.getRatingCount();
        BigDecimal currentAvg = product.getRating();

        BigDecimal total = currentAvg.multiply(BigDecimal.valueOf(count));
        total = total.add(BigDecimal.valueOf(newRating));

        int newCount = count + 1;

        BigDecimal newAvg = total.divide(BigDecimal.valueOf(newCount), 1, RoundingMode.HALF_UP);

        product.setRating(newAvg);
        product.setRatingCount(newCount);
    }

    private void updateProductRating(Product product, float oldRating, float newRating) {

        int count = product.getRatingCount();
        BigDecimal currentAvg = product.getRating();

        BigDecimal total = currentAvg.multiply(BigDecimal.valueOf(count));

        total = total.subtract(BigDecimal.valueOf(oldRating));
        total = total.add(BigDecimal.valueOf(newRating));

        BigDecimal newAvg = total.divide(BigDecimal.valueOf(count), 1, RoundingMode.HALF_UP);

        product.setRating(newAvg);
    }

    private void removeRating(Product product, float rating) {

        int count = product.getRatingCount();

        if (count <= 1) {
            product.setRating(BigDecimal.ZERO);
            product.setRatingCount(0);
            return;
        }

        BigDecimal currentAvg = product.getRating();
        BigDecimal total = currentAvg.multiply(BigDecimal.valueOf(count));

        total = total.subtract(BigDecimal.valueOf(rating));

        int newCount = count - 1;

        BigDecimal newAvg = total.divide(BigDecimal.valueOf(newCount), 1, RoundingMode.HALF_UP);

        product.setRating(newAvg);
        product.setRatingCount(newCount);
    }
}