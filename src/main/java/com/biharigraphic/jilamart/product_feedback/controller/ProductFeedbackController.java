package com.biharigraphic.jilamart.product_feedback.controller;

import com.biharigraphic.jilamart.product_feedback.model.req.CreateFeedbackRequest;
import com.biharigraphic.jilamart.product_feedback.model.res.FeedbackResponse;
import com.biharigraphic.jilamart.product_feedback.service.ProductFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class ProductFeedbackController {

    private final ProductFeedbackService service;

    @PostMapping
    public ResponseEntity<FeedbackResponse> addOrUpdate(
            @RequestHeader("userId") Long userId,
            @RequestBody CreateFeedbackRequest request
    ) {
        return ResponseEntity.ok(service.addOrUpdateFeedback(userId, request));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<FeedbackResponse>> getByProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(service.getProductFeedbacks(productId));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(
            @RequestHeader("userId") Long userId,
            @RequestParam Long productId
    ) {
        service.deleteFeedback(userId, productId);
        return ResponseEntity.ok("Deleted");
    }
}