package com.timebank.userservice.application.dto.response.user;

import com.timebank.userservice.domain.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
	private Long id;
	private String username;
	private String email;
	private String phoneNumber;

	public static UserResponseDto from(User user) {
		return new UserResponseDto(
			user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber()
		);
	}
}
