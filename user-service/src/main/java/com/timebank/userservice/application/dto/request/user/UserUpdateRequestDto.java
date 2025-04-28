package com.timebank.userservice.application.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
	@Size(min = 8, max = 15, message = "비밀번호는 8~15자여야 합니다.")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|;:'\",.<>?/]).{8,15}$",
		message = "비밀번호는 대소문자, 숫자, 특수문자를 포함해야 합니다.")
	private String password;

	@Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
	private String email;

	private String phoneNumber;
}
