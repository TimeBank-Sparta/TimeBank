package com.timebank.userservice.application.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

	@NotBlank(message = "아이디를 입력해주세요.")
	@Size(min = 4, max = 10, message = "아이디는 4~10자여야 합니다.")
	@Pattern(regexp = "^[a-z0-9]+$", message = "아이디는 소문자와 숫자로만 이루어져야 합니다.")
	private String username;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, max = 15, message = "비밀번호는 8~15자여야 합니다.")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|;:'\",.<>?/]).{8,15}$",
		message = "비밀번호는 대소문자, 숫자, 특수문자를 포함해야 합니다.")
	private String password;

	@NotBlank(message = "이메일을 입력해주세요")
	@Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
	private String email;

	@NotBlank(message = "휴대전화 번호를 입력해주세요")
	private String phoneNumber;
	private String adminToken = "";
}
