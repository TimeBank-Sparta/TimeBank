package com.timebank.userservice.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
	//todo: 글자수같은 조건 설정하기
	private String username;
	private String password;
	private String email;
	private String phoneNumber;
	private String adminToken = "";
}
