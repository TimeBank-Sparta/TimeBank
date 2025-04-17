package com.timebank.review.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timebank.review.domain.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	// 거래 관련 리뷰 조회
	List<Review> findByTransactionId(Long transactionId);

	// 특정 사용자가 작성한 리뷰 조회
	List<Review> findByReviewerId(Long reviewerId);

	// 특정 사용자에 대한 리뷰 조회
	List<Review> findByRevieweeId(Long revieweeId);
}
