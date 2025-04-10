package com.timebank.review.application.event;

import java.time.LocalDateTime;

import com.timebank.review.domain.entity.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewEvent {
	private Long reviewId;
	private Long transactionId;
	private Long reviewerId;
	private Long revieweeId;
	private Byte rating;
	private String comment;
	private String eventType;  // 예: CREATED, UPDATED, DELETED

	public ReviewEvent(Review review, String eventType) {
		this.reviewId = review.getReviewId();
		this.transactionId = review.getTransactionId();
		this.reviewerId = review.getReviewerId();
		this.revieweeId = review.getRevieweeId();
		this.rating = review.getRating();
		this.comment = review.getComment();
		this.eventType = eventType;
	}