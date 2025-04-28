package com.timebank.review.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.infrastructure.external.review.dto.ReviewEvent;
import com.timebank.review.domain.entity.Review;
import com.timebank.review.domain.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReviewConsumer {

	private final ReviewRepository reviewRepository;

	private void saveReviewEvent(ReviewEvent event) {
		// 이벤트 타입에 따라 분기 처리
		switch (event.getEventType()) {
			case CREATED -> handleCreated(event);
			case UPDATED -> handleUpdated(event);
			case DELETED -> handleDeleted(event);
			default -> log.warn("처리할 수 없는 이벤트 타입입니다: {}", event.getEventType());
		}
	}

	// CREATED 토픽 소비 메서드
	@KafkaListener(topics = "reviews.events", groupId = "review-group")
	public void consumeReviewEvent(ReviewEvent event) {
		log.info("Consumed ReviewEvent: {}", event);
		// 중복 처리 되어서 주석
		// saveReviewEvent(event);

	}

	private void handleCreated(ReviewEvent event) {
		Review review = Review.builder()
			.transactionId(event.getTransactionId())
			.reviewerId(event.getReviewerId())
			.revieweeId(event.getRevieweeId())
			.rating(event.getRating())
			.comment(event.getComment())
			.build();

		reviewRepository.save(review);
		log.info("Review CREATED and saved: {}", review);
	}

	private void handleUpdated(ReviewEvent event) {
		Review review = reviewRepository.findById(event.getReviewId())
			.orElseThrow(() -> new IllegalStateException("리뷰를 찾을 수 없습니다. ID: " + event.getReviewId()));

		review.setRating(event.getRating());
		review.setComment(event.getComment());

		reviewRepository.save(review);
		log.info("Review UPDATED and saved: {}", review);
	}

	private void handleDeleted(ReviewEvent event) {
		reviewRepository.findById(event.getReviewId()).ifPresentOrElse(
			review -> {
				reviewRepository.delete(review);
				log.info("Review DELETED: {}", review);
			},
			() -> log.warn("삭제할 리뷰를 찾을 수 없습니다. ID: {}", event.getReviewId())
		);
	}
}
