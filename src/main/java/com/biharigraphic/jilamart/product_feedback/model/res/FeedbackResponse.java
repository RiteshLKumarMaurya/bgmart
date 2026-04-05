package com.biharigraphic.jilamart.product_feedback.model.res;

import lombok.Data;

@Data
public class FeedbackResponse {

    private Long id;
    private Long userId;
    private String userName;

    private Float ratingValue;
    private String ratingMessage;
}