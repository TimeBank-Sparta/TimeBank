package com.timebank.helpservice.help_request.presentation.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.helpservice.help_request.application.dto.response.HelpRequestResponse;
import com.timebank.helpservice.help_request.application.service.HelpRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/help-requests")
@RequiredArgsConstructor
public class HelpRequestInternalController {

	private final HelpRequestService helpRequestService;

	@GetMapping("/{helpRequestId}/info")
	public HelpRequestResponse getHelpRequestById(
		@PathVariable Long helpRequestId
	) {
		return helpRequestService.findById(helpRequestId);
	}

	@GetMapping("/{helpRequestId}/exists")
	public boolean existByHelpRequestId(
		@PathVariable Long helpRequestId
	) {
		return helpRequestService.existHelpRequest(helpRequestId);
	}

}
