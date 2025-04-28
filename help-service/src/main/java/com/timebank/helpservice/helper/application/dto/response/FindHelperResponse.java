package com.timebank.helpservice.helper.application.dto.response;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;

import lombok.Builder;

@Builder
public record FindHelperResponse(
	String nickname,
	Double trustScore,
	ApplicantStatus status
) {
	public static FindHelperResponse of(GetUserInfoFeignResponse response, Helper helper) {
		return FindHelperResponse.builder()
			.nickname(response.nickname())
			.trustScore(response.trustScore())
			.status(helper.getApplicantStatus())
			.build();

	}
}
