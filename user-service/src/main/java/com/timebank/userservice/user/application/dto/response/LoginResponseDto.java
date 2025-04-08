package com.timebank.userservice.user.application.dto.response;

import com.timebank.userservice.user.domain.model.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
	private String username;
	private String accessToken;
	private Role role;
}
