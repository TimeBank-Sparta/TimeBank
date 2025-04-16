package com.timebank.helpservice.help_trading.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.timebank.helpservice.help_trading.application.dto.response.ApproveFinishTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.ApproveStartTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.CreateTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.FindHelpTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.RequestFinishTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.RequestStartTradingResponse;
import com.timebank.helpservice.help_trading.application.service.HelpTradingService;
import com.timebank.helpservice.help_trading.presentation.dto.request.CreateTradingRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/help-tradings")
@RequiredArgsConstructor
public class HelpTradingController {

	private final HelpTradingService helpTradingService;

	@PostMapping
	public ResponseEntity<ResponseDto<CreateTradingResponse>> createTrading(
		@RequestBody CreateTradingRequest requestDto
	) {
		return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED,
			helpTradingService.createHelpTrading(requestDto.toCommand())));
	}

	@PostMapping("/{helpTradingsId}/start-request")
	public ResponseEntity<ResponseDto<RequestStartTradingResponse>> requestStartTrading(
		@PathVariable Long helpTradingsId,
		@RequestHeader("X-User-Id") Long userId
	) {
		return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED,
			helpTradingService.requestStartTrading(helpTradingsId, userId)));
	}

	@PostMapping("/{helpTradingsId}/start-approve")
	public ResponseEntity<ResponseDto<ApproveStartTradingResponse>> approveStartTrading(
		@PathVariable Long helpTradingsId,
		@RequestHeader("X-User-Id") Long userId
	) {
		return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED,
			helpTradingService.approveStartTrading(helpTradingsId, userId)));
	}

	@PostMapping("/{helpTradingsId}/finish-request")
	public ResponseEntity<ResponseDto<RequestFinishTradingResponse>> requestFinishTrading(
		@PathVariable Long helpTradingsId,
		@RequestHeader("X-User-Id") Long userId
	) {
		return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED,
			helpTradingService.requestFinishTrading(helpTradingsId, userId)));
	}

	@PostMapping("/{helpTradingsId}/finish-approve")
	public ResponseEntity<ResponseDto<ApproveFinishTradingResponse>> approveFinishTrading(
		@PathVariable Long helpTradingsId,
		@RequestHeader("X-User-Id") Long userId
	) {
		return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED,
			helpTradingService.approveFinishTrading(helpTradingsId, userId)));
	}

	//TODO유저 권한 체크(자기가 작성한 글에 대한 거래내역 조회가능)
	@GetMapping("/{helpRequestId}")
	public ResponseEntity<PageResponseDto<FindHelpTradingResponse>> findByHelpRequestId(
		@PathVariable Long helpRequestId,
		Pageable pageable
	) {
		Page<FindHelpTradingResponse> helpRequestPage =
			helpTradingService.findByHelpRequestId(helpRequestId, pageable);
		return ResponseEntity.ok(new PageResponseDto<>(HttpStatus.OK, helpRequestPage, "조회완료"));
	}

	@DeleteMapping("/{helpRequestId}")
	public ResponseEntity<ResponseDto<Void>> deleteHelpTrading(
		@PathVariable Long helpRequestId
	) {
		helpTradingService.delete(helpRequestId);
		return ResponseEntity.ok(ResponseDto.responseWithNoData(HttpStatus.NO_CONTENT, "삭제완료"));
	}
}
