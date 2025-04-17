package com.timebank.helpservice.helper.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	public ResponseEntity<ResponseDto<CreateHelperResponse>> createHelper(
		@RequestBody CreateHelperRequest requestDto,
		@RequestHeader("X-User-Id") Long userId
	) {
		return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED,
			helperService.createHelper(requestDto.toCommand(), userId)));
	}

	//TODO 유저권한체크(자신의글만 조회가능)
	@GetMapping("/{helpRequestId}")
	public ResponseEntity<PageResponseDto<FindHelperResponse>> findByHelpRequestId(
		@PathVariable Long helpRequestId,
		Pageable pageable
	) {
		Page<FindHelperResponse> helperPage =
			helperService.findByHelpRequestId(helpRequestId, pageable);
		return ResponseEntity.ok(new PageResponseDto<>(HttpStatus.OK, helperPage, "조회완료"));
	}

	@PatchMapping("/{helperId}/accept")
	public ResponseEntity<ResponseDto<Void>> acceptHelper(
		@PathVariable Long helperId
	) {
		helperService.acceptHelper(helperId);
		return ResponseEntity.ok(ResponseDto.responseWithNoData(HttpStatus.OK, "선별완료"));
	}

}
