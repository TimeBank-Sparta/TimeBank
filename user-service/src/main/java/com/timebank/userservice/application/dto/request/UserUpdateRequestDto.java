package com.timebank.userservice.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
	private String password;
	private String email;
	private String phoneNumber;
}
