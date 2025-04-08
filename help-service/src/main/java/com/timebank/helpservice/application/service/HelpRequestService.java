package com.timebank.helpservice.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.helpservice.domain.model.HelpRequest;
import com.timebank.helpservice.domain.repository.HelpRepository;
import com.timebank.helpservice.domain.vo.HelpRequestInfo;
import com.timebank.helpservice.presentation.dto.request.CreateHelpRequestDto;
import com.timebank.helpservice.presentation.dto.request.UpdateHelpRequestDto;
import com.timebank.helpservice.presentation.dto.response.CreateHelpResponseDto;
import com.timebank.helpservice.presentation.dto.response.UpdateHelpResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpRequestService {

	private final HelpRepository helpRepository;

	@Transactional
	public CreateHelpResponseDto createHelpRequest(CreateHelpRequestDto requestDto) {

		return CreateHelpResponseDto.from(helpRepository.save(
			HelpRequest.createOf(HelpRequestInfo.builder()
				.title(requestDto.title())
				.content(requestDto.content())
				.address(requestDto.address())
				.scheduledAt(requestDto.scheduledAt())
				.requiredTime(requestDto.requiredTime())
				.requestedPoint(requestDto.requestedPoint())
				.recruitmentCount(requestDto.recruitmentCount())
				.build())));
	}

	@Transactional
	public UpdateHelpResponseDto updateHelpRequest(UpdateHelpRequestDto requestDto) {

		HelpRequest helpRequest = helpRepository.findById(requestDto.helpRequestId()).orElseThrow(() ->
			new IllegalArgumentException("게시글이 없습니다."));

		helpRequest.update(HelpRequestInfo.builder()
			.title(requestDto.title())
			.content(requestDto.content())
			.address(requestDto.address())
			.scheduledAt(requestDto.scheduledAt())
			.requiredTime(requestDto.requiredTime())
			.requestedPoint(requestDto.requestedPoint())
			.recruitmentCount(requestDto.recruitmentCount())
			.postStatus(requestDto.postStatus())
			.build()
		);

		return UpdateHelpResponseDto.from(helpRequest);
	}

}
