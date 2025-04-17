package com.timebank.userservice.presentation.controller.profile;

import java.util.List;

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

import com.timebank.common.application.dto.ResponseDto;
import com.timebank.userservice.application.dto.request.profile.UserProfileCreateRequestDto;
import com.timebank.userservice.application.dto.request.profile.UserProfileUpdateRequestDto;
import com.timebank.userservice.application.dto.response.profile.UserMyProfileResponseDto;
import com.timebank.userservice.application.dto.response.profile.UserProfileResponseDto;
import com.timebank.userservice.application.service.profile.UserProfileService;
import com.timebank.userservice.presentation.dto.response.GetUserInfoFeignResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-profiles")
public class UserProfileController {

	private final UserProfileService userProfileService;

	// 프로필 생성
	@PostMapping
	public ResponseEntity<ResponseDto<UserProfileResponseDto>> createProfile(
		@RequestHeader("X-User-Id") Long userId,
		@RequestBody @Valid UserProfileCreateRequestDto request
	) {
		UserProfileResponseDto response = userProfileService.createProfile(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.success(HttpStatus.CREATED, response));
	}

	// 내 프로필 조회
	@GetMapping("/me")
	public ResponseEntity<ResponseDto<UserMyProfileResponseDto>> getMyProfile(
		@RequestHeader("X-User-Id") Long userId
	) {
		UserMyProfileResponseDto response = userProfileService.getMyProfile(userId);
		return ResponseEntity.ok(ResponseDto.success(HttpStatus.OK, response));
	}

	// 다른 사람 프로필 조회 (닉네임 기반)
	@GetMapping("/nickname/{nickname}")
	public ResponseEntity<ResponseDto<UserProfileResponseDto>> getProfileByNickname(
		@PathVariable String nickname
	) {
		UserProfileResponseDto response = userProfileService.getProfileByNickname(nickname);
		return ResponseEntity.ok(ResponseDto.success(HttpStatus.OK, response));
	}

	// 다른 사람 프로필 조회 (닉네임 기반)
	@GetMapping("/{userId}")
	public ResponseEntity<ResponseDto<UserProfileResponseDto>> getProfileByUserId(
		@PathVariable Long userId
	) {
		UserProfileResponseDto response = userProfileService.getProfileByUserId(userId);
		return ResponseEntity.ok(ResponseDto.success(HttpStatus.OK, response));
	}

	// 프로필 수정
	@PutMapping
	public ResponseEntity<ResponseDto<UserProfileResponseDto>> updateProfile(
		@RequestHeader("X-User-Id") Long userId,
		@RequestBody @Valid UserProfileUpdateRequestDto request
	) {
		UserProfileResponseDto response = userProfileService.updateProfile(userId, request);
		return ResponseEntity.ok(ResponseDto.success(HttpStatus.OK, response));
	}

	// 프로필 삭제
	@DeleteMapping
	public ResponseEntity<ResponseDto<Void>> deleteProfile(
		@RequestHeader("X-User-Id") Long userId
	) {
		userProfileService.deleteProfile(userId);
		return ResponseEntity.noContent().build();
	}

	// 지원자 리스트 조회하기
	@PostMapping("/apply-list")
	public List<GetUserInfoFeignResponse> getUserInfoByHelper(
		@RequestBody List<Long> requestList
	) {
		return userProfileService.getUserInfoList(requestList);
	}
}
