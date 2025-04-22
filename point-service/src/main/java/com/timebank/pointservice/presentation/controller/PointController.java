package com.timebank.pointservice.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.ResponseDto;
import com.timebank.pointservice.application.dto.GetAccountResponseDto;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.presentation.dto.HoldPointsRequestDto;
import com.timebank.pointservice.presentation.dto.PointTransferRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

	private final PointService pointService;

	// 계정 생성
	@PostMapping("/{userId}")
	public ResponseEntity<ResponseDto<PointAccount>> createAccount(@PathVariable Long userId) {
		PointAccount createdAccount = pointService.createAccount(userId);
		return ResponseEntity.ok(ResponseDto.success(createdAccount));
	}

	// 계정 조회
	@GetMapping("/{userId}")
	public ResponseEntity<ResponseDto<GetAccountResponseDto>> getAccount(@PathVariable Long userId) {
		GetAccountResponseDto dto = pointService.getAccount(userId);
		return ResponseEntity.ok(ResponseDto.success(dto));
	}

	// 포인트 송금
	@PostMapping("/transfer")
	public ResponseEntity<ResponseDto<String>> transferPoints(@Valid @RequestBody PointTransferRequest request) {
		pointService.transferPoints(request.toCommand());
		return ResponseEntity.ok(ResponseDto.success("포인트 전송이 완료되었습니다."));
	}

	// 글 작성 시 포인트 보류
	@PostMapping("/hold")
	public ResponseEntity<ResponseDto<Void>> holdPointsForPost(@Valid @RequestBody HoldPointsRequestDto request) {
		pointService.holdPointsForPost(request.getUserId(), request.getAmount());
		return ResponseEntity.ok(ResponseDto.responseWithNoData(
			HttpStatus.NO_CONTENT, "성공적으로 처리되었습니다."));
	}
}