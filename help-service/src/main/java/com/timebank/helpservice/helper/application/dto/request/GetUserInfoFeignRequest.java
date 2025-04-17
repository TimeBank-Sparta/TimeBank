package com.timebank.helpservice.helper.application.dto.request;

import com.timebank.helpservice.helper.domain.model.Helper;

import lombok.Builder;

@Builder
public record GetUserInfoFeignRequest(Long userId) {
	public static GetUserInfoFeignRequest from(Helper helper) {
		return GetUserInfoFeignRequest.builder()
			.userId(helper.getUserId())
			.build();
	}
}
