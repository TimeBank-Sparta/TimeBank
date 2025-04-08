package com.timebank.helpservice.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.helpservice.domain.model.HelpRequest;
import com.timebank.helpservice.domain.model.HelpTrading;
import com.timebank.helpservice.domain.repository.HelpRepository;
import com.timebank.helpservice.domain.vo.HelpTradingInfo;
import com.timebank.helpservice.presentation.dto.request.CreateTradingRequestDto;
import com.timebank.helpservice.presentation.dto.response.CreateTradingResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpTradingService {
	private final HelpRepository helpRepository;

	@Transactional
	public CreateTradingResponseDto createHelpTrading(CreateTradingRequestDto requestDto) {
		HelpRequest helpRequest = helpRepository.findById(requestDto.helpRequestId()).orElseThrow(() ->
			new IllegalArgumentException("게시글이 없습니다."));

		HelpTrading helpTrading = HelpTrading.createOf(
			HelpTradingInfo.builder()
				.requesterId(requestDto.requesterId())
				.helperId(requestDto.helperId())
				.actualPoints(requestDto.actualPoints())
				.build());

		helpRequest.addHelpTrading(helpTrading);
		helpRepository.save(helpRequest);

		return CreateTradingResponseDto.of(helpTrading);
	}
}
