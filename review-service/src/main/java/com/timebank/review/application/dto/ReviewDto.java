package com.timebank.review.application.dto;

import com.timebank.review.domain.entity.Review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

	@NotNull(message = "거래 ID는 필수입니다.")
	@Positive(message = "거래 ID는 양수여야 합니다.")
	private Long transactionId;

	@NotNull(message = "작성자 ID는 필수입니다.")
	@Positive(message = "작성자 ID는 양수여야 합니다.")
	private Long reviewerId;

	@NotNull(message = "피작성자 ID는 필수입니다.")
	@Positive(message = "피작성자 ID는 양수여야 합니다.")
	private Long revieweeId;

	@Min(value = 1, message = "평점은 1 이상이어야 합니다.")
	@Max(value = 5, message = "평점은 5 이하여야 합니다.")
	private int rating;

	@NotBlank(message = "코멘트는 비어있을 수 없습니다.")
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