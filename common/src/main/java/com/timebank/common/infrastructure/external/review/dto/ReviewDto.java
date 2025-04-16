package com.timebank.common.infrastructure.external.review.dto;

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
	private int rating;
	private String comment;

}