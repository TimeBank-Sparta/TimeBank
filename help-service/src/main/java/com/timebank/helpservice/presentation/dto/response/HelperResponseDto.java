package com.timebank.helpservice.presentation.dto.response;

import com.timebank.helpservice.domain.ApplicantStatus;
import com.timebank.helpservice.domain.model.Helper;

import lombok.Builder;

@Builder
public record HelperResponseDto(
	Long helperId,
	Long userId,
	ApplicantStatus applicantStatus
) {
	public static HelperResponseDto from(Helper helper) {
		return HelperResponseDto.builder()
			.helperId(helper.getId())
			.userId(helper.getUserId())
			.applicantStatus(helper.getApplicantStatus())
			.build();
	}

}
