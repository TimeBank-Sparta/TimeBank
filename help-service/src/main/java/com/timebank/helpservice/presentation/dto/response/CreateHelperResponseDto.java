package com.timebank.helpservice.presentation.dto.response;

import com.timebank.helpservice.domain.ApplicantStatus;
import com.timebank.helpservice.domain.model.Helper;

import lombok.Builder;

@Builder
public record CreateHelperResponseDto(
	Long userId,
	ApplicantStatus applicantStatus
) {
	public static CreateHelperResponseDto from(Helper helper) {
		return CreateHelperResponseDto.builder()
			.userId(helper.getUserId())
			.applicantStatus(helper.getApplicantStatus())
			.build();
	}
}
