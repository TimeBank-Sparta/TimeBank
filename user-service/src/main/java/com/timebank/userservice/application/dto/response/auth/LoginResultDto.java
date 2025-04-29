package com.timebank.userservice.application.dto.response.auth;

public class LoginResultDto {
	private final LoginResponseDto responseDto;
	private final String refreshToken;

	public LoginResultDto(LoginResponseDto dto, String refreshToken) {
		this.responseDto = dto;
		this.refreshToken = refreshToken;
	}

	public LoginResponseDto getResponseDto() {
		return responseDto;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
}
