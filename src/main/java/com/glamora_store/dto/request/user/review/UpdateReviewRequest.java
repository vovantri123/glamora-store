package com.glamora_store.dto.request.user.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReviewRequest {

    @NotNull(message = "REVIEW_RATING_REQUIRED")
    @Min(value = 1, message = "REVIEW_RATING_INVALID")
    @Max(value = 5, message = "REVIEW_RATING_INVALID")
    private Integer rating;

    @Size(max = 2000, message = "REVIEW_COMMENT_TOO_LONG")
    private String comment;
}