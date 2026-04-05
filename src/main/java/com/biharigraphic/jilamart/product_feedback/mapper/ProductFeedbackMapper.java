package com.biharigraphic.jilamart.product_feedback.mapper;

import com.biharigraphic.jilamart.product.entity.Product;
import com.biharigraphic.jilamart.product_feedback.entity.ProductFeedback;
import com.biharigraphic.jilamart.product_feedback.model.req.CreateFeedbackRequest;
import com.biharigraphic.jilamart.product_feedback.model.res.FeedbackResponse;
import com.biharigraphic.jilamart.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProductFeedbackMapper {

    public ProductFeedback toEntity(CreateFeedbackRequest req, User user, Product product) {

        return ProductFeedback.builder()
                .user(user)
                .product(product)
                .ratingValue(req.getRatingValue())
                .ratingMessage(req.getRatingMessage())
                .build();
    }

    public FeedbackResponse toResponse(ProductFeedback feedback) {

        FeedbackResponse res = new FeedbackResponse();

        res.setId(feedback.getId());
        res.setUserId(feedback.getUser().getId());

        if (feedback.getUser().getFullName()!=null)
            res.setUserName(feedback.getUser().getFullName()); // adjust field
        else
            res.setUserName(feedback.getUser().getUsername()); // adjust field

        res.setRatingValue(feedback.getRatingValue());
        res.setRatingMessage(feedback.getRatingMessage());

        return res;
    }
}