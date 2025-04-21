package com.timebank.helpservice.help_trading.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.PageResponseDto;
import com.timebank.common.application.dto.ResponseDto;
import com.timebank.helpservice.help_request.application.dto.response.HelpRequestResponse;
import com.timebank.helpservice.help_trading.application.dto.response.ApproveFinishTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.ApproveStartTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.CancelTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.CreateTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.FindHelpTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.RequestFinishTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.RequestStartTradingResponse;
import com.timebank.helpservice.help_trading.application.service.HelpTradingService;
import com.timebank.helpservice.help_trading.presentation.dto.request.CreateTradingRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Validated
@RestController
@RequestMapping("/api/v1/help-tradings")
@RequiredArgsConstructor
public class HelpTradingController {

	private final HelpTradingService helpTradingService;

	@PostMapping
	public ResponseEntity<ResponseDto<CreateTradingResponse>> createTrading(
		@Valid @RequestBody CreateTradingRequest requestDto
	) {
		CreateTradingResponse response = helpTradingService.createHelpTrading(requestDto.toCommand());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ResponseDto.success(response));
	}

	@PostMapping("/{helpTradingsId}/start-request")
	public ResponseEntity<ResponseDto<RequestStartTradingResponse>> requestStartTrading(
		@PathVariable Long helpTradingsId,
		@RequestHeader("X-User-Id") Long userId
	) {
		RequestStartTradingResponse response = helpTradingService.requestStartTrading(helpTradingsId, userId));
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ResponseDto.success(response));
	}

	@PostMapping("/{helpTradingsId}/start-approve")
	public ResponseEntity<ResponseDto<ApproveStartTradingResponse>> approveStartTrading(
		@PathVariable Long helpTradingsId,
		@RequestHeader("X-User-Id") Long userId
	) {
		ApproveStartTradingResponse response = helpTradingService.approveStartTrading(helpTradingsId, userId);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ResponseDto.success(response));
	}

	@PostMapping("/{helpTradingsId}/finish-request")
	public ResponseEntity<ResponseDto<RequestFinishTradingResponse>> requestFinishTrading(
		@PathVariable Long helpTradingsId,
		@RequestHeader("X-User-Id") Long userId
	) {
		RequestFinishTradingResponse response = helpTradingService.requestFinishTrading(helpTradingsId, userId);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ResponseDto.success(response));
	}

	@PostMapping("/{helpTradingsId}/finish-approve")
	public ResponseEntity<ResponseDto<ApproveFinishTradingResponse>> approveFinishTrading(
		@PathVariable Long helpTradingsId,
		@RequestHeader("X-User-Id") Long userId
	) {
		ApproveFinishTradingResponse response = helpTradingService.approveFinishTrading(helpTradingsId, userId);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ResponseDto.success(response));
	}

	@PostMapping("/{helpTradingsId}/cancel-trading")
	public ResponseEntity<ResponseDto<CancelTradingResponse>> cancelTrading(
		@PathVariable Long helpTradingsId,
		@RequestHeader("X-User-Id") Long userId
	) {
		CancelTradingResponse response = helpTradingService.cancelTrading(helpTradingsId, userId);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ResponseDto.success(response));
	}

	//TODO유저 권한 체크(자기가 작성한 글에 대한 거래내역 조회가능)
	@GetMapping("/{helpRequestId}")
	public ResponseEntity<PageResponseDto<FindHelpTradingResponse>> findByHelpRequestId(
		@PathVariable Long helpRequestId,
		Pageable pageable
	) {
		Page<FindHelpTradingResponse> helpRequestPage =
			helpTradingService.findByHelpRequestId(helpRequestId, pageable);
		PageResponseDto<FindHelpTradingResponse> responseDto = new PageResponseDto<>(
			HttpStatus.OK, helpRequestPage, "조회 완료");

		return ResponseEntity.ok(ResponseDto.success(responseDto).getData());
	}

	@DeleteMapping("/{helpRequestId}")
	public ResponseEntity<ResponseDto<Void>> deleteHelpTrading(
		@PathVariable Long helpRequestId,
		@RequestHeader("X-User-Id") String userId
	) {
		helpTradingService.delete(helpRequestId, userId);
		return ResponseEntity.ok(ResponseDto.responseWithNoData(
			HttpStatus.NO_CONTENT, "삭제완료"));	}
}
