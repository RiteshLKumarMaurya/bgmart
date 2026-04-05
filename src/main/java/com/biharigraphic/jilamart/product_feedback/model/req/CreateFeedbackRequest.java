package com.biharigraphic.jilamart.product_feedback.model.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateFeedbackRequest {

    private Long productId;

    @Min(1)
    @Max(5)
    private Float ratingValue;

    private String ratingMessage;
}