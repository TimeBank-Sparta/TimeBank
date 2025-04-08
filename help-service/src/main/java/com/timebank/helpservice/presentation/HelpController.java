package com.timebank.helpservice.presentation;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.helpservice.application.HelpFacade;
import com.timebank.helpservice.presentation.dto.request.CreateHelpRequestDto;
import com.timebank.helpservice.presentation.dto.request.CreateHelperRequestDto;
import com.timebank.helpservice.presentation.dto.request.CreateTradingRequestDto;
import com.timebank.helpservice.presentation.dto.request.UpdateHelpRequestDto;
import com.timebank.helpservice.presentation.dto.response.CreateHelpResponseDto;
import com.timebank.helpservice.presentation.dto.response.CreateHelperResponseDto;
import com.timebank.helpservice.presentation.dto.response.CreateTradingResponseDto;
import com.timebank.helpservice.presentation.dto.response.UpdateHelpResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HelpController {

	private final HelpFacade helpFacade;

	@PostMapping("/help_requests")
	public CreateHelpResponseDto createHelpRequest(
		@RequestBody CreateHelpRequestDto requestDto
	) {
		return helpFacade.createHelpRequest(requestDto);
	}

	@PatchMapping("/help_requests/{help_requests_id}")
	public UpdateHelpResponseDto updateHelpRequest(
		@RequestBody UpdateHelpRequestDto requestDto
	) {
		return helpFacade.updateHelpRequest(requestDto);
	}

	@PostMapping("/helpers")
	public CreateHelperResponseDto createHelper(
		@RequestBody CreateHelperRequestDto requestDto
	) {
		return helpFacade.createHelper(requestDto);
	}

	@PostMapping("/tradings")
	public CreateTradingResponseDto createTrading(
		@RequestBody CreateTradingRequestDto requestDto
	) {
		return helpFacade.createTrading(requestDto);
	}
}
