package com.timebank.userservice.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.timebank.userservice.application.event.ReviewEvent;
import com.timebank.userservice.application.event.ReviewEventType;
import com.timebank.userservice.application.service.profile.UserProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewConsumer {
	private final UserProfileService userProfileService;

	@KafkaListener(topics = "reviews.events", groupId = "user-service", containerFactory = "")
	public void consume(ReviewEvent event) {
		try {
			if (event.getEventType() == ReviewEventType.CREATED) {
				userProfileService.updateRating(event.getRevieweeId(), event.getRating(), 1);
			} else if (event.getEventType() == ReviewEventType.UPDATED) {
				userProfileService.updateRating(event.getRevieweeId(), event.getRating(), 0);
			} else if (event.getEventType() == ReviewEventType.DELETED) {
				userProfileService.updateRating(event.getRevieweeId(), -event.getRating(), -1);
			}
		} catch (Exception e) {
			log.error("Failed to update rating for user {}: {}", event.getRevieweeId(), e.getMessage());
		}
	}
}
