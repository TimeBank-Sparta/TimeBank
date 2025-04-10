package com.timebank.helpservice.help_request.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.helpservice.help_request.application.dto.response.CreateHelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.HelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.UpdateHelpRequestResponse;
import com.timebank.helpservice.help_request.application.service.HelpRequestService;
import com.timebank.helpservice.help_request.presentation.dto.request.CreateHelpRequest;
import com.timebank.helpservice.help_request.presentation.dto.request.SearchHelpRequest;
import com.timebank.helpservice.help_request.presentation.dto.request.UpdateHelpRequest;
import com.timebank.helpservice.help_request.presentation.dto.response.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/help-requests")
@RequiredArgsConstructor
public class HelpRequestController {

	private final HelpRequestService helpRequestService;

	@PostMapping
	public CreateHelpRequestResponse createHelpRequest(
		@RequestBody CreateHelpRequest requestDto
	) {
		return helpRequestService.createHelpRequest(requestDto.toCommand());
	}

	@GetMapping("/{helpRequestId}")
	public HelpRequestResponse findByHelpRequestId(
		@PathVariable Long helpRequestId
	) {
		return helpRequestService.findById(helpRequestId);
	}

	@GetMapping("/search")
	public PageResponse<HelpRequestResponse> searchHelpRequest(
		SearchHelpRequest request,
		Pageable pageable
	) {
		return PageResponse.from(helpRequestService.searchHelpRequest(request, pageable));
	}

	@PatchMapping("/{helpRequestId}")
	public UpdateHelpRequestResponse updateHelpRequest(
		@RequestBody UpdateHelpRequest requestDto,
		@PathVariable Long helpRequestId
	) {
		return helpRequestService.updateHelpRequest(requestDto.toCommand(), helpRequestId);
	}

	@PatchMapping("/{helpRequestId}/complete")
	public UpdateHelpRequestResponse completeHelpRequest(
		@PathVariable Long helpRequestId
	) {
		return helpRequestService.completeHelpRequest(helpRequestId);
	}

	@DeleteMapping("/{helpRequestId}")
	public String deleteHelpRequest(
		@PathVariable Long helpRequestId
	) {
		helpRequestService.deleteHelpRequest(helpRequestId);
		return "deleted";
	}

}
