package com.timebank.userservice.application.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.timebank.userservice.application.dto.request.LoginRequestDto;
import com.timebank.userservice.application.dto.request.SignUpRequestDto;
import com.timebank.userservice.application.dto.response.LoginResponseDto;
import com.timebank.userservice.domain.jwt.JwtProvider;
import com.timebank.userservice.domain.model.user.Role;
import com.timebank.userservice.domain.model.user.User;
import com.timebank.userservice.domain.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	//todo:환경변수로 관리하기
	private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

	//회원가입
	public User signUp(SignUpRequestDto requestDto) {
		Role role = Role.USER;
		if (requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
			role = Role.ADMIN;
		}

		User user = User.of(
			requestDto.getUsername(),
			passwordEncoder.encode(requestDto.getPassword()),
			requestDto.getEmail(),
			requestDto.getPhoneNumber(),
			role);

		userRepository.save(user);

		return user;
	}

	//로그인
	public LoginResponseDto login(LoginRequestDto requestDto) {
		User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
			() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
		);
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("회원정보가 일치하지 않습니다.");
		}

		//토큰 생성
		String accessToken = jwtProvider.createAccessToken(user.getId(), user.getRole());

		return new LoginResponseDto(user.getUsername(), accessToken, user.getRole());
	}
}
