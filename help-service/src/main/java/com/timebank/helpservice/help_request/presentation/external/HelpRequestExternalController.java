package com.timebank.helpservice.help_request.presentation.external;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.PageResponseDto;
import com.timebank.common.application.dto.ResponseDto;
import com.timebank.helpservice.help_request.application.dto.response.CreateHelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.HelpRequestResponse;
import com.timebank.helpservice.help_request.application.dto.response.UpdateHelpRequestResponse;
import com.timebank.helpservice.help_request.application.service.HelpRequestService;
import com.timebank.helpservice.help_request.presentation.dto.request.CreateHelpRequest;
import com.timebank.helpservice.help_request.presentation.dto.request.SearchHelpRequest;
import com.timebank.helpservice.help_request.presentation.dto.request.UpdateHelpRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/help-requests")
@RequiredArgsConstructor
public class HelpRequestExternalController {

	private final HelpRequestService helpRequestService;

	@PostMapping
	public ResponseEntity<ResponseDto<CreateHelpRequestResponse>> createHelpRequest(
		@RequestBody CreateHelpRequest requestDto
	) {
		return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED,
			helpRequestService.createHelpRequest(requestDto.toCommand())));
	}

	@GetMapping("/{helpRequestId}")
	public ResponseEntity<ResponseDto<HelpRequestResponse>> findByHelpRequestId(
		@PathVariable Long helpRequestId
	) {
		return ResponseEntity.ok(ResponseDto.success(
			helpRequestService.findById(helpRequestId)));
	}

	@GetMapping("/search")
	public ResponseEntity<PageResponseDto<HelpRequestResponse>> searchHelpRequest(
		SearchHelpRequest request,
		Pageable pageable
	) {
		Page<HelpRequestResponse> helpRequestResponses =
			helpRequestService.searchHelpRequest(request.toQuery(), pageable);

		return ResponseEntity.ok(new PageResponseDto<>(
			HttpStatus.OK, helpRequestResponses, "조회 완료"));
	}

	@PatchMapping("/{helpRequestId}")
	public ResponseEntity<ResponseDto<UpdateHelpRequestResponse>> updateHelpRequest(
		@RequestBody UpdateHelpRequest requestDto,
		@PathVariable Long helpRequestId
	) {
		return ResponseEntity.ok(ResponseDto.success(
			helpRequestService.updateHelpRequest(requestDto.toCommand(), helpRequestId)));
	}

	@PatchMapping("/{helpRequestId}/complete")
	public ResponseEntity<ResponseDto<UpdateHelpRequestResponse>> completeHelpRequest(
		@PathVariable Long helpRequestId
	) {
		return ResponseEntity.ok(ResponseDto.success(
			helpRequestService.completeHelpRequest(helpRequestId)));
	}

	@DeleteMapping("/{helpRequestId}")
	public ResponseEntity<ResponseDto<Void>> deleteHelpRequest(
		@PathVariable Long helpRequestId
	) {
		helpRequestService.deleteHelpRequest(helpRequestId);

		return ResponseEntity.ok(ResponseDto.responseWithNoData(
			HttpStatus.NO_CONTENT, "삭제완료"));
	}

}
