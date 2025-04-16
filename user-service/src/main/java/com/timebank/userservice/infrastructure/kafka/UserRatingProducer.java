package com.timebank.userservice.infrastructure.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.timebank.userservice.domain.kafka.UserRatingMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRatingProducer {

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final String TOPIC = "user-rating-updated";

	public void sendRatingUpdate(Long userId, double newAverageRating) {
		UserRatingMessage message = new UserRatingMessage(userId, newAverageRating);
		kafkaTemplate.send(TOPIC, message);
	}
}
