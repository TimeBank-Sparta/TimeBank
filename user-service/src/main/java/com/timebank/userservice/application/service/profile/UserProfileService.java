package com.timebank.userservice.application.service.profile;

import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.infrastructure.external.notification.dto.NotificationEvent;
import com.timebank.common.infrastructure.external.notification.dto.NotificationEventType;
import com.timebank.common.infrastructure.external.notification.dto.NotificationType;
import com.timebank.userservice.application.dto.request.profile.UserProfileCreateRequestDto;
import com.timebank.userservice.application.dto.request.profile.UserProfileUpdateRequestDto;
import com.timebank.userservice.application.dto.response.profile.UserProfileResponseDto;
import com.timebank.userservice.domain.model.profile.UserProfile;
import com.timebank.userservice.domain.model.user.User;
import com.timebank.userservice.infrastructure.persistence.JpaUserProfileRepository;
import com.timebank.userservice.infrastructure.persistence.JpaUserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

	private final JpaUserRepository userRepository;
	private final JpaUserProfileRepository userProfileRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	// 공통적으로 사용할 기본 Kafka 토픽명은 실제 이벤트 발행 시 각 이벤트 타입의 토픽명을 사용할 예정입니다.
	// 즉, NotificationEventType.CREATED.getTopic(), UPDATED, DELETED 등으로 전송

	/**
	 * 프로필 생성 시
	 */
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
		UserProfile savedProfile = userProfileRepository.save(profile);

		// 프로필 생성 이벤트 발행: CREATED 이벤트
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(user.getId())
			.type(NotificationType.USER_PROFILE_CREATE)
			.message("프로필이 생성되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.CREATED)
			.build();
		kafkaTemplate.send(NotificationEventType.CREATED.getTopic(), event);
		log.info("프로필 생성 이벤트 발행: {}", event);

		return UserProfileResponseDto.from(savedProfile);
	}

	/**
	 * 본인 프로필 조회
	 */
	public UserProfileResponseDto getMyProfile(Long userId) {
		UserProfile profile = userProfileRepository.findByUserId(userId)
			.orElseThrow(() -> new EntityNotFoundException("프로필이 없습니다."));
		return UserProfileResponseDto.from(profile);
	}

	/**
	 * 닉네임으로 다른 사람 프로필 조회
	 */
	public UserProfileResponseDto getProfileByNickname(String nickname) {
		UserProfile profile = userProfileRepository.findByNickname(nickname)
			.orElseThrow(() -> new EntityNotFoundException("해당 닉네임의 프로필이 없습니다."));
		return UserProfileResponseDto.from(profile);
	}

	/**
	 * 프로필 수정 시
	 */
	@Transactional
	public UserProfileResponseDto updateProfile(Long userId, UserProfileUpdateRequestDto request) {
		UserProfile profile = userProfileRepository.findByUserId(userId)
			.orElseThrow(() -> new EntityNotFoundException("유저 ID: " + userId + " 에 대한 프로필을 찾을 수 없습니다."));

		profile.update(
			request.getNickname(),
			request.getHelpServices(),
			request.getNeedServices(),
			request.getLocation(),
			request.getIntroduction()
		);
		UserProfile updatedProfile = userProfileRepository.save(profile);

		// 프로필 수정 이벤트 발행: UPDATED 이벤트
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(profile.getUser().getId())
			.type(NotificationType.USER_PROFILE_UPDATE)
			.message("프로필이 수정되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.UPDATED)
			.build();
		kafkaTemplate.send(NotificationEventType.UPDATED.getTopic(), event);
		log.info("프로필 수정 이벤트 발행: {}", event);

		return UserProfileResponseDto.from(updatedProfile);
	}

	/**
	 * 프로필 삭제 시
	 */
	@Transactional
	public void deleteProfile(Long userId) {
		UserProfile profile = userProfileRepository.findByUserId(userId)
			.orElseThrow(() -> new EntityNotFoundException("유저 ID: " + userId + " 에 대한 프로필을 찾을 수 없습니다."));

		profile.delete(userId.toString());
		userProfileRepository.delete(profile);

		// 프로필 삭제 이벤트 발행: DELETED 이벤트
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(userId)
			.type(NotificationType.USER_PROFILE_UPDATE) // 필요에 따라 USER_PROFILE_DELETED로 별도 추가 가능
			.message("프로필이 삭제되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.DELETED)
			.build();
		kafkaTemplate.send(NotificationEventType.DELETED.getTopic(), event);
		log.info("프로필 삭제 이벤트 발행: {}", event);
	}
}