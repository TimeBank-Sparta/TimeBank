package com.timebank.helpservice.help_trading.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.PageResponseDto;
import com.timebank.common.application.dto.ResponseDto;
import com.timebank.helpservice.help_trading.application.dto.response.CreateTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.FindHelpTradingResponse;
import com.timebank.helpservice.help_trading.application.service.HelpTradingService;
import com.timebank.helpservice.help_trading.presentation.dto.request.CreateTradingRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/help-tradings")
@RequiredArgsConstructor
public class HelpTradingController {

	private final HelpTradingService helpTradingService;

	@PostMapping
	public ResponseDto<CreateTradingResponse> createTrading(
		@RequestBody CreateTradingRequest requestDto
	) {
		return new ResponseDto<>(HttpStatus.CREATED,
			helpTradingService.createHelpTrading(requestDto.toCommand()));
	}

	//TODO유저 권한 체크(자기가 작성한 글에 대한 내역 조회가능)
	@GetMapping("/{helpRequestId}")
	public PageResponseDto<FindHelpTradingResponse> findByHelpRequestId(
		@PathVariable Long helpRequestId,
		Pageable pageable
	) {
		Page<FindHelpTradingResponse> helpRequestPage =
			helpTradingService.findByHelpRequestId(helpRequestId, pageable);
		return new PageResponseDto<>(HttpStatus.OK, helpRequestPage, "조회완료");
	}

	@DeleteMapping("/{helpRequestId}")
	public ResponseDto<Void> deleteHelpTrading(
		@PathVariable Long helpRequestId
	) {
		helpTradingService.delete(helpRequestId);
		return ResponseDto.responseWithNoData(HttpStatus.NO_CONTENT, "삭제완료");
	}
}
