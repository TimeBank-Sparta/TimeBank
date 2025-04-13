package com.timebank.review.application.dto;

import com.timebank.review.domain.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

	private Long reviewId;
	private Long transactionId;
	private Long reviewerId;
	private Long revieweeId;
	private Byte rating;
	private String comment;

	public static ReviewDto fromEntity(Review review) {
		return new ReviewDto(
			review.getReviewId(),
			review.getTransactionId(),
			review.getReviewerId(),
			review.getRevieweeId(),
			review.getRating(),
			review.getComment()
		);
	}
}