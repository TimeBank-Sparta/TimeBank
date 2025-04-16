package com.timebank.common.infrastructure.external.review;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.timebank.common.application.dto.PageResponseDto;
import com.timebank.common.application.dto.ResponseDto;
import com.timebank.common.infrastructure.external.review.dto.ReviewDto;

@FeignClient(name = "review-service")
public interface ReviewClient {

	@PostMapping
	ResponseDto<ReviewDto> createReview(@RequestBody ReviewDto reviewDto);

	@GetMapping("/{reviewId}")
	ResponseDto<ReviewDto> getReview(@PathVariable("reviewId") Long reviewId);

	@GetMapping
	PageResponseDto<ReviewDto> getAllReviews(Pageable pageable);

	@GetMapping("/transactions/{transactionId}")
	ResponseDto<List<ReviewDto>> getReviewsByTransaction(@PathVariable("transactionId") Long transactionId);

	@GetMapping("/users/{userId}")
	ResponseDto<List<ReviewDto>> getReviewsByReviewer(@PathVariable("userId") Long userId);

	@PatchMapping("/{reviewId}")
	ResponseDto<ReviewDto> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewDto reviewDto);

	@DeleteMapping("/{reviewId}")
	ResponseDto<String> deleteReview(@PathVariable("reviewId") Long reviewId);
}