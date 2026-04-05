package com.biharigraphic.jilamart.product_feedback.service;

import com.biharigraphic.jilamart.product_feedback.model.req.CreateFeedbackRequest;
import com.biharigraphic.jilamart.product_feedback.model.res.FeedbackResponse;

import java.util.List;

public interface ProductFeedbackService {

    FeedbackResponse addOrUpdateFeedback(Long userId, CreateFeedbackRequest request);

    List<FeedbackResponse> getProductFeedbacks(Long productId);

    void deleteFeedback(Long userId, Long productId);
}