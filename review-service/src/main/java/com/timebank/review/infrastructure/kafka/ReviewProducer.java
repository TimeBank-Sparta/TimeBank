package com.timebank.review.infrastructure.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.timebank.review.application.event.ReviewEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	private String reviewTopic = "review-topic";

	public void sendReviewEvent(ReviewEvent event) {
		kafkaTemplate.send(reviewTopic, event);
	}
}
