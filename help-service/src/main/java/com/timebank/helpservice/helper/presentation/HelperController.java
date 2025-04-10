package com.timebank.helpservice.helper.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.helpservice.help_request.presentation.dto.response.PageResponse;
import com.timebank.helpservice.helper.application.dto.response.CreateHelperResponse;
import com.timebank.helpservice.helper.application.dto.response.FindHelperResponse;
import com.timebank.helpservice.helper.application.dto.response.UpdateHelperResponse;
import com.timebank.helpservice.helper.application.service.HelperService;
import com.timebank.helpservice.helper.presentation.dto.request.CreateHelperRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/helpers")
@RequiredArgsConstructor
public class HelperController {

	private final HelperService helperService;

	@PostMapping
	public CreateHelperResponse createHelper(
		@RequestBody CreateHelperRequest requestDto
	) {
		return helperService.createHelper(requestDto.toCommand());
	}

	//TODO 유저권한체크(자신의글만 조회가능)
	@GetMapping("/helpers/{helpRequestId}")
	public PageResponse<FindHelperResponse> findByHelpRequestId(
		@PathVariable Long helpRequestId,
		Pageable pageable
	) {
		return PageResponse.from(helperService.findByHelpRequestId(helpRequestId, pageable));
	}

	@PatchMapping("/helpers/{helperId}/accept")
	public UpdateHelperResponse acceptHelper(
		@PathVariable Long helperId
	) {
		return helperService.acceptHelper(helperId);
	}

}
