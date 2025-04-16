package com.timebank.helpservice.helper.application.dto.response;

public record GetUserInfoFeignResponse(
	Long userId,
	String username
) {
}
