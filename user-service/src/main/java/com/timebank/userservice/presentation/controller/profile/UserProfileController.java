package com.timebank.userservice.presentation.controller.profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.userservice.application.dto.request.profile.UserProfileCreateRequestDto;
import com.timebank.userservice.application.dto.request.profile.UserProfileUpdateRequestDto;
import com.timebank.userservice.application.dto.response.profile.UserProfileResponseDto;
import com.timebank.userservice.application.service.profile.UserProfileService;
import com.timebank.userservice.domain.model.profile.UserProfile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-profiles")
public class UserProfileController {

	private final UserProfileService userProfileService;

	// 프로필 생성
	@PostMapping
	public ResponseEntity<UserProfileResponseDto> createProfile(
		@RequestHeader("X-User-Id") Long userId,
		@RequestBody @Valid UserProfileCreateRequestDto request
	) {
		UserProfileResponseDto response = userProfileService.createProfile(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 내 프로필 조회
	@GetMapping("/me")
	public ResponseEntity<UserProfileResponseDto> getMyProfile(
		@RequestHeader("X-User-Id") Long userId
	) {
		UserProfile profile = userProfileService.getMyProfile(userId);
		return ResponseEntity.ok(UserProfileResponseDto.from(profile));
	}

	// 다른 사람 프로필 조회 (닉네임 기반)
	@GetMapping("/{nickname}")
	public ResponseEntity<UserProfileResponseDto> getProfileByNickname(
		@PathVariable String nickname
	) {
		UserProfile profile = userProfileService.getProfileByNickname(nickname);
		return ResponseEntity.ok(UserProfileResponseDto.from(profile));
	}

	// 프로필 수정
	@PutMapping
	public ResponseEntity<UserProfileResponseDto> updateProfile(
		@RequestHeader("X-User-Id") Long userId,
		@RequestBody @Valid UserProfileUpdateRequestDto request
	) {
		UserProfileResponseDto response = userProfileService.updateProfile(userId, request);
		return ResponseEntity.ok(response);
	}

	// 프로필 삭제
	@DeleteMapping
	public ResponseEntity<Void> deleteProfile(
		@RequestHeader("X-User-Id") Long userId
	) {
		userProfileService.deleteProfile(userId);
		return ResponseEntity.noContent().build();
	}
}
