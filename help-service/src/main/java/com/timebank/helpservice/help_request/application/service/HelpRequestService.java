package com.timebank.helpservice.help_request.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.application.exception.CustomNotFoundException;
import com.timebank.helpservice.help_request.application.dto.request.CreateHelpRequestCommand;
import com.timebank.helpservice.help_request.application.dto.request.UpdateHelpRequestCommand;
import com.timebank.helpservice.help_request.application.dto.response.CreateHelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.HelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.UpdateHelpRequestResponse;
import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.HelpRequestRepository;
import com.timebank.helpservice.help_request.presentation.dto.request.SearchHelpRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpRequestService {

	private final HelpRequestRepository helpRepository;

	@Transactional
	public CreateHelpRequestResponse createHelpRequest(CreateHelpRequestCommand command) {

		return CreateHelpRequestResponse.from(helpRepository.save(
			HelpRequest.createFrom(command.toHelpRequestInfo())));
	}

	@Transactional(readOnly = true)
	public HelpRequestResponse findById(Long helpRequestId) {
		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);

		return HelpRequestResponse.from(helpRequest);
	}

	@Transactional(readOnly = true)
	public Page<HelpRequestResponse> searchHelpRequest(
		SearchHelpRequest request, Pageable pageable
	) {
		return helpRepository.search(request.toQuery().toHelpRequestQuery(), pageable)
			.map(HelpRequestResponse::from);
	}

	@Transactional
	public UpdateHelpRequestResponse updateHelpRequest(
		UpdateHelpRequestCommand command, Long helpRequestId
	) {
		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);
		helpRequest.update(command.toHelpRequestInfo());

		return UpdateHelpRequestResponse.from(helpRequest);
	}

	@Transactional
	public UpdateHelpRequestResponse completeHelpRequest(Long helpRequestId) {
		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);

		//TODO 승인된 지원자 제외 전체 삭제

		helpRequest.completePostStatus();

		return UpdateHelpRequestResponse.from(helpRequest);
	}

	public void deleteHelpRequest(Long helpRequestId) {

		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);

		//helpRequest.delete();
	}

	private HelpRequest getHelpRequestOrThrow(Long helpRequestId) {
		return helpRepository.findById(helpRequestId)
			.orElseThrow(() -> new CustomNotFoundException("게시글이 없습니다."));
	}

}
