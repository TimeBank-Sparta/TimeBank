package com.timebank.userservice.application.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.userservice.application.dto.request.UserUpdateRequestDto;
import com.timebank.userservice.application.dto.response.UserResponseDto;
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

		String password = passwordEncoder.encode(requestDto.getPassword());

		user.updateUser(password, requestDto.getEmail(), requestDto.getPhoneNumber());

		return UserResponseDto.from(user);
	}

	@Transactional
	public void deleteUser(Long id, String currentId) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
		if (!user.getId().equals(Long.parseLong(currentId))) {
			throw new IllegalArgumentException("동일 회원이 아닙니다.");
		}
		//todo:common 적용 후 삭제
	}
}
