package com.timebank.userservice.application.service.profile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.userservice.application.dto.request.profile.UserProfileCreateRequestDto;
import com.timebank.userservice.application.dto.request.profile.UserProfileUpdateRequestDto;
import com.timebank.userservice.application.dto.response.profile.UserProfileResponseDto;
import com.timebank.userservice.domain.model.profile.UserProfile;
import com.timebank.userservice.domain.model.user.User;
import com.timebank.userservice.domain.repository.profile.UserProfileRepository;
import com.timebank.userservice.domain.repository.user.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {

	private final UserRepository userRepository;
	private final UserProfileRepository userProfileRepository;

	@Transactional
	public UserProfileResponseDto createProfile(Long userId, UserProfileCreateRequestDto request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("유저 ID: " + userId + " 에 대한 유저를 찾을 수 없습니다."));

		if (userProfileRepository.findByUserId(userId).isPresent()) {
			throw new IllegalStateException("이미 프로필이 존재합니다.");
		}

		UserProfile profile = UserProfile.of(
			user,
			request.getNickname(),
			request.getHelpServices(),
			request.getNeedServices(),
			request.getLocation(),
			request.getIntroduction()
		);

		return UserProfileResponseDto.from(userProfileRepository.save(profile));
	}

	// 본인 프로필 조회용
	public UserProfile getMyProfile(Long userId) {
		return userProfileRepository.findByUserId(userId)
			.orElseThrow(() -> new EntityNotFoundException("프로필이 없습니다."));
	}

	// 닉네임으로 다른 사람 프로필 조회
	public UserProfile getProfileByNickname(String nickname) {
		return userProfileRepository.findByNickname(nickname)
			.orElseThrow(() -> new EntityNotFoundException("해당 닉네임의 프로필이 없습니다."));
	}

	@Transactional
	public UserProfileResponseDto updateProfile(Long userId, UserProfileUpdateRequestDto request) {
		UserProfile profile = userProfileRepository.findByUserId(userId)
			.orElseThrow(() -> new EntityNotFoundException("유저 ID: " + userId + " 에 대한 유저를 찾을 수 없습니다."));

		profile.update(
			request.getNickname(),
			request.getHelpServices(),
			request.getNeedServices(),
			request.getLocation(),
			request.getIntroduction()
		);

		return UserProfileResponseDto.from(profile);
	}

	@Transactional
	public void deleteProfile(Long userId) {
		UserProfile profile = userProfileRepository.findByUserId(userId)
			.orElseThrow(() -> new EntityNotFoundException("유저 ID: " + userId + " 에 대한 유저를 찾을 수 없습니다."));

		//todo:common적용 후 작업하기
	}
}
