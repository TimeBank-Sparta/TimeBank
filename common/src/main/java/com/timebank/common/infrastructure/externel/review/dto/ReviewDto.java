package com.timebank.common.infrastructure.externel.review.dto;

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

}