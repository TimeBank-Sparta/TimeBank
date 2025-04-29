package com.timebank.userservice.application.service.user;

import static org.springframework.util.StringUtils.*;

import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.infrastructure.external.notification.dto.NotificationEvent;
import com.timebank.common.infrastructure.external.notification.dto.NotificationEventType;
import com.timebank.common.infrastructure.external.notification.dto.NotificationType;
import com.timebank.userservice.application.dto.request.user.UserUpdateRequestDto;
import com.timebank.userservice.application.dto.response.user.UserResponseDto;
import com.timebank.userservice.domain.model.user.Role;
import com.timebank.userservice.domain.model.user.User;
import com.timebank.userservice.infrastructure.persistence.JpaUserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final JpaUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	// 로그인 처리: 사용자 로그인 성공 후 알림 이벤트 발행
	public UserResponseDto processLogin(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

		// 로그인 성공 이벤트 생성 (생성 이벤트)
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(user.getId())
			.type(NotificationType.USER_LOGIN)  // 유저 관련 알림: 로그인
			.message("로그인에 성공하였습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.CREATED)  // 이벤트 유형: CREATED
			.build();

		kafkaTemplate.send(NotificationEventType.CREATED.getTopic(), event);
		log.info("로그인 이벤트 발행: {}", event);

		return UserResponseDto.from(user);
	}

	public UserResponseDto getUser(Long id, Role role) {
		if (Role.USER.equals(role)) {
			throw new AccessDeniedException("접근 권한이 없습니다.");
		}
		User user = userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
		return UserResponseDto.from(user);
	}

	// 회원정보 수정: 업데이트 이벤트 발행
	@Transactional
	public UserResponseDto updateUser(Long id, String currentId, UserUpdateRequestDto requestDto) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

		if (!user.getId().equals(Long.parseLong(currentId))) {
			log.info("현재 사용자: {}", currentId);
			throw new IllegalArgumentException("동일 회원이 아닙니다.");
		}

		boolean allFieldsEmpty =
			!hasText(requestDto.getPassword()) &&
				!hasText(requestDto.getEmail()) &&
				!hasText(requestDto.getPhoneNumber());

		if (allFieldsEmpty) {
			throw new IllegalArgumentException("수정할 내용이 없습니다.");
		}

		String encodedPassword =
			hasText(requestDto.getPassword())
				? passwordEncoder.encode(requestDto.getPassword())
				: null;

		user.updateUser(encodedPassword, requestDto.getEmail(), requestDto.getPhoneNumber());

		// 회원정보 수정 이벤트 생성 (업데이트 이벤트)
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(user.getId())
			.type(NotificationType.USER_INFO_UPDATE)  // 유저 관련 알림: 회원정보 수정
			.message("회원 정보가 성공적으로 수정되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.UPDATED)
			.build();

		kafkaTemplate.send(NotificationEventType.UPDATED.getTopic(), event);
		log.info("회원정보 수정 이벤트 발행: {}", event);

		return UserResponseDto.from(user);
	}

	// 회원 탈퇴: 삭제 이벤트 발행
	@Transactional
	public void deleteUser(Long id, String currentId) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

		if (!user.getId().equals(Long.parseLong(currentId))) {
			throw new IllegalArgumentException("동일 회원이 아닙니다.");
		}
		
		user.delete(currentId);
		user.getUserProfile().delete(currentId);

		// 회원 탈퇴(삭제) 이벤트 생성
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(user.getId())
			.type(NotificationType.USER_INFO_UPDATE) // 필요한 경우 별도의 타입(USER_DELETE)로 변경 가능
			.message("회원 탈퇴가 처리되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.DELETED)
			.build();

		kafkaTemplate.send(NotificationEventType.DELETED.getTopic(), event);
		log.info("회원 탈퇴 이벤트 발행: {}", event);
	}
}
