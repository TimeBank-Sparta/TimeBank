package com.timebank.userservice.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.timebank.userservice.application.event.ReviewEvent;
import com.timebank.userservice.application.event.ReviewEventType;
import com.timebank.userservice.domain.service.UserRatingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewConsumer {
	private final UserRatingService userRatingService;

	@KafkaListener(topics = "review-topic", groupId = "user-service")
	public void consume(ReviewEvent event) {
		if (event.getEventType() == ReviewEventType.CREATED || event.getEventType() == ReviewEventType.UPDATED) {
			userRatingService.updateAverageRating(event.getRevieweeId());
		}
	}
}
