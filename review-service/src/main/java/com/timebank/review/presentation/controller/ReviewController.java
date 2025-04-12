package com.timebank.review.presentation.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.PageResponseDto;
import com.timebank.common.application.dto.ResponseDto;
import com.timebank.review.application.dto.ReviewDto;
import com.timebank.review.application.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * 리뷰 등록
	 * POST /api/v1/reviews
	 */
	@PostMapping
	public ResponseEntity<ResponseDto<ReviewDto>> createReview(@RequestBody @Valid ReviewDto reviewDto) {
		ReviewDto created = reviewService.createReview(reviewDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ResponseDto.success(HttpStatus.CREATED, created));
	}

	/**
	 * 특정 리뷰 조회
	 * GET /api/v1/reviews/{review_id}
	 */
	@GetMapping("/{review_id}")
	public ResponseEntity<ResponseDto<ReviewDto>> getReview(@PathVariable("review_id") Long reviewId) {
		ReviewDto review = reviewService.getReview(reviewId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ResponseDto.success(HttpStatus.OK, review));
	}

	/**
	 * 전체 리뷰 조회
	 * GET /api/v1/reviews
	 */
	@GetMapping
	public ResponseEntity<PageResponseDto<ReviewDto>> getAllReviews(Pageable pageable) {
		Page<ReviewDto> reviews = reviewService.getAllReviews(pageable);
		PageResponseDto<ReviewDto> responseDto = PageResponseDto.success(
			HttpStatus.OK, reviews, "Reviews fetched successfully");
		return ResponseEntity.status(HttpStatus.OK)
			.body(responseDto);
	}

	/**
	 * 특정 거래의 리뷰 조회
	 * GET /api/v1/transactions/{transaction_id}/reviews
	 */
	@GetMapping("/transactions/{transaction_id}")
	public ResponseEntity<ResponseDto<List<ReviewDto>>> getReviewsByTransaction(
		@PathVariable("transaction_id") Long transactionId) {
		List<ReviewDto> reviews = reviewService.getReviewsByTransaction(transactionId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ResponseDto.success(HttpStatus.OK, reviews));
	}

	/**
	 * 특정 사용자가 작성한 리뷰 조회
	 * GET /api/v1/users/{user_id}/reviews
	 */
	@GetMapping("/users/{user_id}")
	public ResponseEntity<ResponseDto<List<ReviewDto>>> getReviewsByReviewer(
		@PathVariable("user_id") Long reviewerId) {
		List<ReviewDto> reviews = reviewService.getReviewsByReviewer(reviewerId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ResponseDto.success(HttpStatus.OK, reviews));
	}

	/**
	 * 특정 리뷰 수정 (PATCH 방식)
	 * PATCH /api/v1/reviews/{review_id}
	 */
	@PatchMapping("/{review_id}")
	public ResponseEntity<ResponseDto<ReviewDto>> updateReview(
		@PathVariable("review_id") Long reviewId,
		@RequestBody @Valid ReviewDto reviewDto) {
		ReviewDto updated = reviewService.updateReview(reviewId, reviewDto);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ResponseDto.success(HttpStatus.OK, updated));
	}

	/**
	 * 특정 리뷰 삭제
	 * DELETE /api/v1/reviews/{review_id}
	 */
	@DeleteMapping("/{review_id}")
	public ResponseEntity<ResponseDto<String>> deleteReview(
		@PathVariable("review_id") Long reviewId) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ResponseDto.success(HttpStatus.OK, "Review deleted successfully"));
	}
}