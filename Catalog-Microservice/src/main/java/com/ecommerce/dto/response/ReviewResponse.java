package com.ecommerce.dto.response;

import com.ecommerce.model.ReviewStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long reviewId;

    private Long userId;

    private Integer rating;

    private String title;

    private String comment;

    private ReviewStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}