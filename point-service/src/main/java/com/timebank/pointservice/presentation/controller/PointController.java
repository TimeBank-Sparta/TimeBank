package com.timebank.pointservice.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.pointservice.application.dto.getAccountResponseDto;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.presentation.dto.HoldPointsRequestDto;
import com.timebank.pointservice.presentation.dto.PointTransferRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

	private final PointService pointService;

	// 계정 생성
	@PostMapping
	public ResponseEntity<PointAccount> createAccount(@PathVariable Long userId) {
		PointAccount createdAccount = pointService.createAccount(userId);
		return ResponseEntity.ok(createdAccount);
	}

	// 계정 조회
	@GetMapping("/{userId}")
	public ResponseEntity<getAccountResponseDto> getAccount(@PathVariable Long userId) {
		getAccountResponseDto dto = pointService.getAccount(userId);
		return ResponseEntity.ok(dto);
	}


	// 포인트 송금
	@PostMapping("/transfer")
	public ResponseEntity<String> transferPoints(@RequestBody PointTransferRequest request) {
		pointService.transferPoints(request.toCommand());
		return ResponseEntity.ok("포인트 전송이 완료되었습니다.");
	}

	// 글 작성 시 포인트 보류
	@PostMapping("/hold")
	public ResponseEntity<Void> holdPointsForPost(@RequestBody HoldPointsRequestDto request) {
		pointService.holdPointsForPost(request.getUserId(), request.getAmount());
		return ResponseEntity.ok().build();
	}
}