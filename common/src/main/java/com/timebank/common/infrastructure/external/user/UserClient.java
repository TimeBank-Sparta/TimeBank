package com.timebank.common.infrastructure.external.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.timebank.common.application.dto.ResponseDto;
import com.timebank.common.infrastructure.config.FeignConfig;
import com.timebank.common.infrastructure.external.user.dto.UserResponseDto;
import com.timebank.common.infrastructure.external.user.dto.UserUpdateRequestDto;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient {

	@GetMapping("/api/v1/users/{id}")
	ResponseEntity<ResponseDto<UserResponseDto>> getUser(@PathVariable("id") Long id);

	@PutMapping("/api/v1/users/{id}")
	ResponseEntity<ResponseDto<UserResponseDto>> updateUser(
		@PathVariable("id") Long id,
		@RequestHeader("X-User-Id") String currentId,
		@RequestBody UserUpdateRequestDto userUpdateRequestDto
	);

	@DeleteMapping("/api/v1/users/{id}")
	ResponseEntity<Void> deleteUser(
		@PathVariable("id") Long id,
		@RequestHeader("X-User-Id") String currentId
	);
}