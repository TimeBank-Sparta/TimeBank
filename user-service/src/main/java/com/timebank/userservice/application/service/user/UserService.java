package com.timebank.userservice.application.service.user;

import static org.springframework.util.StringUtils.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.userservice.application.dto.request.user.UserUpdateRequestDto;
import com.timebank.userservice.application.dto.response.user.UserResponseDto;
import com.timebank.userservice.domain.model.user.User;
import com.timebank.userservice.domain.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserResponseDto getUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
		return UserResponseDto.from(user);
	}

	@Transactional
	public UserResponseDto updateUser(Long id, String currentId, UserUpdateRequestDto requestDto) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

		if (!user.getId().equals(Long.parseLong(currentId))) {
			log.info(currentId);
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
			(requestDto.getPassword() != null && !requestDto.getPassword().isEmpty())
				? passwordEncoder.encode(requestDto.getPassword())
				: null;

		user.updateUser(encodedPassword, requestDto.getEmail(), requestDto.getPhoneNumber());

		return UserResponseDto.from(user);
	}

	@Transactional
	public void deleteUser(Long id, String currentId) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
		if (!user.getId().equals(Long.parseLong(currentId))) {
			throw new IllegalArgumentException("동일 회원이 아닙니다.");
		}
		user.delete(currentId);
	}
}
