package com.timebank.review.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAverageUpdatedEvent {
	private Long userId;         // revieweeId
	private double averageRating;
}
