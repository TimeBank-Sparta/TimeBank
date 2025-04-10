package com.timebank.helpservice.helper.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.PageResponseDto;
import com.timebank.common.application.dto.ResponseDto;
import com.timebank.helpservice.helper.application.dto.response.CreateHelperResponse;
import com.timebank.helpservice.helper.application.dto.response.FindHelperResponse;
import com.timebank.helpservice.helper.application.service.HelperService;
import com.timebank.helpservice.helper.presentation.dto.request.CreateHelperRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/helpers")
@RequiredArgsConstructor
public class HelperController {

	private final HelperService helperService;

	@PostMapping
	public ResponseDto<CreateHelperResponse> createHelper(
		@RequestBody CreateHelperRequest requestDto
	) {
		return new ResponseDto<>(HttpStatus.CREATED,
			helperService.createHelper(requestDto.toCommand()));
	}

	//TODO 유저권한체크(자신의글만 조회가능)
	@GetMapping("/helpers/{helpRequestId}")
	public PageResponseDto<FindHelperResponse> findByHelpRequestId(
		@PathVariable Long helpRequestId,
		Pageable pageable
	) {
		Page<FindHelperResponse> helperPage =
			helperService.findByHelpRequestId(helpRequestId, pageable);
		return new PageResponseDto<>(HttpStatus.OK, helperPage, "조회완료");
	}

	@PatchMapping("/helpers/{helperId}/accept")
	public ResponseDto<Void> acceptHelper(
		@PathVariable Long helperId
	) {
		helperService.acceptHelper(helperId);
		return ResponseDto.responseWithNoData(HttpStatus.NO_CONTENT, "삭제완료");
	}

}
