package com.timebank.review.domain.entity;

import com.timebank.common.domain.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reviews")
public class Review extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId;

	@Column(name = "transaction_id", nullable = false)
	private Long transactionId;

	@Column(name = "reviewer_id", nullable = false)
	private Long reviewerId;

	@Column(name = "reviewee_id", nullable = false)
	private Long revieweeId;

	@Column(nullable = false)
	private int rating; // 1~5 점

	@Column(columnDefinition = "TEXT")
	private String comment;

	public Review(Long transactionId, Long reviewerId, Long revieweeId, int rating, String comment) {
		this.transactionId = transactionId;
		this.reviewerId = reviewerId;
		this.revieweeId = revieweeId;
		this.rating = rating;
		this.comment = comment;
	}

}