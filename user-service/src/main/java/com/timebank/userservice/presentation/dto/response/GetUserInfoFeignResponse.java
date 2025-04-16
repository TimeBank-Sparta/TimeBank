package com.timebank.userservice.presentation.dto.response;

public record GetUserInfoFeignResponse(
	Long userId,
	String nickname
) {
}