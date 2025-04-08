package com.timebank.userservice.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
	private String username;
	private String password;
	private String email;
	private String phoneNumber;
	private String adminToken = "";
}
