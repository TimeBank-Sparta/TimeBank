package com.timebank.helpservice.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.helpservice.domain.ApplicantStatus;
import com.timebank.helpservice.domain.model.HelpRequest;
import com.timebank.helpservice.domain.model.Helper;
import com.timebank.helpservice.domain.repository.HelpRepository;
import com.timebank.helpservice.presentation.dto.request.CreateHelperRequestDto;
import com.timebank.helpservice.presentation.dto.response.CreateHelperResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelperService {
	private final HelpRepository helpRepository;

	@Transactional
	public CreateHelperResponseDto createHelper(CreateHelperRequestDto requestDto) {
		HelpRequest helpRequest = helpRepository.findById(requestDto.helpRequestId())
			.orElseThrow(() -> new RuntimeException("게시글이 없습니다.")
			);

		Helper helper = Helper.builder()
			.userId(requestDto.userId())
			.applicantStatus(ApplicantStatus.SUPPORTED)
			.build();

		helpRequest.addHelper(helper);

		helpRepository.save(helpRequest);

		return CreateHelperResponseDto.from(helper);
	}
}
