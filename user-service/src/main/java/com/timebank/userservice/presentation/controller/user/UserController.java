package com.timebank.userservice.presentation.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.userservice.application.dto.request.user.UserUpdateRequestDto;
import com.timebank.userservice.application.dto.response.user.UserResponseDto;
import com.timebank.userservice.application.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDto> getUser(@PathVariable long id) {
		UserResponseDto userResponseDto = userService.getUser(id);
		return ResponseEntity.ok(userResponseDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDto> updateUser(
		@PathVariable Long id,
		@RequestHeader("X-User-Id") String currentId,
		@RequestBody UserUpdateRequestDto userUpdateRequestDto
	) {
		UserResponseDto userResponseDto = userService.updateUser(id, currentId, userUpdateRequestDto);
		return ResponseEntity.ok(userResponseDto);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id, @RequestHeader("X-User-Id") String currentId) {
		userService.deleteUser(id, currentId);
	}
}
