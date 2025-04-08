package com.timebank.userservice.application.dto.response;

import com.timebank.userservice.domain.model.user.Role;

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
