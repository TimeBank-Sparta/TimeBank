package com.timebank.review.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.review.application.dto.ReviewDto;
import com.timebank.review.domain.entity.Review;
import com.timebank.review.domain.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;

	public ReviewDto createReview(ReviewDto reviewDto) {
		Review review = new Review(
			reviewDto.getTransactionId(),
			reviewDto.getReviewerId(),
			reviewDto.getRevieweeId(),
			reviewDto.getRating(),
			reviewDto.getComment()
		);
		Review savedReview = reviewRepository.save(review);
		return ReviewDto.fromEntity(savedReview);
	}

	public ReviewDto getReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
		return ReviewDto.fromEntity(review);
	}

	public List<ReviewDto> getAllReviews() {
		return reviewRepository.findAll().stream()
			.map(ReviewDto::fromEntity)
			.collect(Collectors.toList());
	}

	public List<ReviewDto> getReviewsByTransaction(Long transactionId) {
		return reviewRepository.findByTransactionId(transactionId).stream()
			.map(ReviewDto::fromEntity)
			.collect(Collectors.toList());
	}

	public List<ReviewDto> getReviewsByReviewer(Long reviewerId) {
		return reviewRepository.findByReviewerId(reviewerId).stream()
			.map(ReviewDto::fromEntity)
			.collect(Collectors.toList());
	}

	public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
		// 업데이트할 필드만 수정 (평점, 코멘트 등)
		review.setRating(reviewDto.getRating());
		review.setComment(reviewDto.getComment());
		Review updated = reviewRepository.save(review);
		return ReviewDto.fromEntity(updated);
	}

	public void deleteReview(Long reviewId) {
		if (!reviewRepository.existsById(reviewId)) {
			throw new EntityNotFoundException("Review not found with id: " + reviewId);
		}
		reviewRepository.deleteById(reviewId);
	}
}