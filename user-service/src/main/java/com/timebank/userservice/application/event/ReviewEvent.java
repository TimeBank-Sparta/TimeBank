package com.timebank.userservice.application.event;

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
	private int rating;
	private String comment;
	private ReviewEventType eventType;  // 예: CREATED, UPDATED, DELETED
}
