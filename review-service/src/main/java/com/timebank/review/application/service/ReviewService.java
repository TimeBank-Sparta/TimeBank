package com.timebank.review.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.review.application.dto.ReviewDto;
import com.timebank.review.application.event.ReviewEvent;
import com.timebank.review.domain.entity.Review;
import com.timebank.review.domain.entity.ReviewEventType;
import com.timebank.review.domain.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final String reviewTopic = "reviews.events";

	/**
	 * 리뷰 생성 시, 리뷰 저장 후 "CREATED" 이벤트를 Kafka로 발행합니다.
	 */
	public ReviewDto createReview(ReviewDto reviewDto) {
		Review review = new Review(
			reviewDto.getTransactionId(),
			reviewDto.getReviewerId(),
			reviewDto.getRevieweeId(),
			reviewDto.getRating(),
			reviewDto.getComment()
		);
		Review savedReview = reviewRepository.save(review);

		// Kafka 이벤트 발행: 리뷰 생성 이벤트
		ReviewEvent event = new ReviewEvent(savedReview, ReviewEventType.CREATED);
		kafkaTemplate.send(reviewTopic, event);

		return ReviewDto.fromEntity(savedReview);
	}

	/**
	 * 리뷰 ID로 리뷰 조회
	 */
	public ReviewDto getReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
		return ReviewDto.fromEntity(review);
	}

	/**
	 * 전체 리뷰 조회 (페이지네이션 적용)
	 */
	public Page<ReviewDto> getAllReviews(Pageable pageable) {
		Page<Review> reviews = reviewRepository.findAll(pageable);
		return reviews.map(ReviewDto::fromEntity);
	}

	/**
	 * 거래 관련 리뷰 조회
	 */
	public List<ReviewDto> getReviewsByTransaction(Long transactionId) {
		List<Review> review = reviewRepository.findByTransactionId(transactionId)
			.orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + transactionId));
		return review.stream()
			.map(ReviewDto::fromEntity)
			.collect(Collectors.toList());
	}

	/**
	 * 특정 사용자가 작성한 리뷰 조회
	 */
	public List<ReviewDto> getReviewsByReviewer(Long reviewerId) {
		List<Review> review = reviewRepository.findByReviewerId(reviewerId)
			.orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewerId));
		return review.stream()
			.map(ReviewDto::fromEntity)
			.collect(Collectors.toList());
	}

	/**
	 * 리뷰 수정 시, 업데이트 후 "UPDATED" 이벤트를 Kafka로 발행합니다.
	 */
	public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
		// 업데이트할 필드(평점, 코멘트 등) 수정
		review.setRating(reviewDto.getRating());
		review.setComment(reviewDto.getComment());
		Review updated = reviewRepository.save(review);

		// Kafka 이벤트 발행: 리뷰 업데이트 이벤트
		ReviewEvent event = new ReviewEvent(updated, ReviewEventType.UPDATED);
		kafkaTemplate.send(reviewTopic, event);

		return ReviewDto.fromEntity(updated);
	}

	/**
	 * 리뷰 삭제 시, 삭제 후 "DELETED" 이벤트를 Kafka로 발행합니다.
	 */
	public void deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
		reviewRepository.deleteById(reviewId);

		// Kafka 이벤트 발행: 리뷰 삭제 이벤트
		ReviewEvent event = new ReviewEvent(review, ReviewEventType.DELETED);
		kafkaTemplate.send(reviewTopic, event);
	}

}