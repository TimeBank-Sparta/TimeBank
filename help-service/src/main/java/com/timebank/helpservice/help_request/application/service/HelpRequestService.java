package com.timebank.helpservice.help_request.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.application.exception.CustomNotFoundException;
import com.timebank.helpservice.help_request.application.dto.request.CreateHelpRequestCommand;
import com.timebank.helpservice.help_request.application.dto.request.HelpRequestToHelperKafkaDto;
import com.timebank.helpservice.help_request.application.dto.request.SearchHelpRequestQuery;
import com.timebank.helpservice.help_request.application.dto.request.UpdateHelpRequestCommand;
import com.timebank.helpservice.help_request.application.dto.response.CreateHelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.HelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.UpdateHelpRequestResponse;
import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.HelpRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpRequestService {

	private final HelpRequestRepository helpRepository;
	private final HelpRequestEventProducer eventProducer;

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
		SearchHelpRequestQuery request, Pageable pageable
	) {
		return helpRepository.search(request.toHelpRequestQuery(), pageable)
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
		//모집완료
		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);
		eventProducer.sendToHelper(HelpRequestToHelperKafkaDto.of(helpRequestId));
		helpRequest.completePostStatus();

		return UpdateHelpRequestResponse.from(helpRequest);
	}

	public void deleteHelpRequest(Long helpRequestId) {

		HelpRequest helpRequest = getHelpRequestOrThrow(helpRequestId);

		//helpRequest.delete();
	}

	public boolean existHelpRequest(Long helpRequestId) {
		return helpRepository.existsById(helpRequestId);
	}

	private HelpRequest getHelpRequestOrThrow(Long helpRequestId) {
		return helpRepository.findById(helpRequestId)
			.orElseThrow(() -> new CustomNotFoundException("게시글이 없습니다."));
	}

}
