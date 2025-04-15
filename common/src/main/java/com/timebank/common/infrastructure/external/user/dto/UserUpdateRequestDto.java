package com.timebank.common.infrastructure.external.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDto {
	private String password;
	private String email;
	private String phoneNumber;
}
