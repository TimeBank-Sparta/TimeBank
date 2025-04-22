package com.timebank.userservice.presentation.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.ResponseDto;
import com.timebank.userservice.application.dto.request.user.UserUpdateRequestDto;
import com.timebank.userservice.application.dto.response.user.UserResponseDto;
import com.timebank.userservice.application.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<ResponseDto<UserResponseDto>> getUser(@PathVariable long id) {
		UserResponseDto userResponseDto = userService.getUser(id);
		return ResponseEntity.ok(ResponseDto.success(userResponseDto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseDto<UserResponseDto>> updateUser(
		@PathVariable Long id,
		@RequestHeader("X-User-Id") String currentId,
		@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto
	) {
		UserResponseDto userResponseDto = userService.updateUser(id, currentId, userUpdateRequestDto);
		return ResponseEntity.ok(ResponseDto.success(userResponseDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDto<Void>> deleteUser(
		@PathVariable Long id,
		@RequestHeader("X-User-Id") String currentId
	) {
		userService.deleteUser(id, currentId);
		return ResponseEntity.ok(ResponseDto
			.responseWithNoData(HttpStatus.NO_CONTENT, "성공적으로 처리되었습니다."));
	}
}
