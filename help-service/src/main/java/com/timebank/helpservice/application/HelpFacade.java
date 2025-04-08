package com.timebank.helpservice.application;

import org.springframework.stereotype.Component;

import com.timebank.helpservice.application.service.HelpRequestService;
import com.timebank.helpservice.application.service.HelpTradingService;
import com.timebank.helpservice.application.service.HelperService;
import com.timebank.helpservice.presentation.dto.request.CreateHelpRequestDto;
import com.timebank.helpservice.presentation.dto.request.CreateHelperRequestDto;
import com.timebank.helpservice.presentation.dto.request.CreateTradingRequestDto;
import com.timebank.helpservice.presentation.dto.request.UpdateHelpRequestDto;
import com.timebank.helpservice.presentation.dto.response.CreateHelpResponseDto;
import com.timebank.helpservice.presentation.dto.response.CreateHelperResponseDto;
import com.timebank.helpservice.presentation.dto.response.CreateTradingResponseDto;
import com.timebank.helpservice.presentation.dto.response.UpdateHelpResponseDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HelpFacade {
	private final HelpRequestService helpRequestService;
	private final HelperService helperService;
	private final HelpTradingService helpTradingService;

	public CreateHelpResponseDto createHelpRequest(CreateHelpRequestDto requestDto) {
		return helpRequestService.createHelpRequest(requestDto);
	}

	public UpdateHelpResponseDto updateHelpRequest(UpdateHelpRequestDto requestDto) {
		return helpRequestService.updateHelpRequest(requestDto);
	}

	public CreateHelperResponseDto createHelper(CreateHelperRequestDto requestDto) {
		return helperService.createHelper(requestDto);
	}

	public CreateTradingResponseDto createTrading(CreateTradingRequestDto requestDto) {
		return helpTradingService.createHelpTrading(requestDto);
	}
}
