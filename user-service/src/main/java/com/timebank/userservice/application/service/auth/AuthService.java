package com.timebank.userservice.application.service.auth;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.userservice.application.dto.request.auth.LoginRequestDto;
import com.timebank.userservice.application.dto.request.auth.SignUpRequestDto;
import com.timebank.userservice.application.dto.response.auth.LoginResponseDto;
import com.timebank.userservice.domain.jwt.JwtProvider;
import com.timebank.userservice.domain.model.user.Role;
import com.timebank.userservice.domain.model.user.User;
import com.timebank.userservice.infrastructure.client.PointServiceClient;
import com.timebank.userservice.infrastructure.persistence.JpaUserRepository;
import com.timebank.userservice.presentation.dto.request.RefreshTokenRequestDto;
import com.timebank.userservice.presentation.dto.response.TokenResponseDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private final JpaUserRepository userRepository;
	private final RefreshTokenService refreshTokenService;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final PointServiceClient pointServiceClient;

	//todo:환경변수로 관리하기
	private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

	//회원가입
	@Transactional
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
		pointServiceClient.creatAccount(user.getId());
		return user;
	}

	//로그인
	@Transactional
	public LoginResponseDto login(LoginRequestDto requestDto) {
		User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
			() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다.")
		);
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("회원정보가 일치하지 않습니다.");
		}

		//토큰 생성
		String accessToken = jwtProvider.createAccessToken(user.getId(), user.getRole());
		String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getRole());

		user.updateRefreshToken(refreshToken);
		userRepository.save(user); //rdbs에 저장
		refreshTokenService.save(user.getId(), refreshToken); // Redis 저장

		return new LoginResponseDto(user.getUsername(), accessToken, refreshToken, user.getRole());
	}

	//리프레쉬토큰으로 새로운 accessToken발급받기
	public TokenResponseDto refreshTokenRedis(RefreshTokenRequestDto requestDto) {
		log.info("authService의 refreshToken 메서드 실행!!");
		String requestToken = requestDto.getRefreshToken();
		log.info("RefreshToken을 이제 검증 시작할게요!!");
		// 1. 위변조 및 만료 체크 (서명 무효 or exp 만료)
		if (!jwtProvider.validateToken(requestToken)) {
			Long userId = null;
			try {
				userId = jwtProvider.extractUserIdIgnoreExpiration(requestToken); // 아래에서 구현
				refreshTokenService.delete(userId); // Redis에서 토큰 삭제
			} catch (Exception e) {
				// parsing 실패 시도 방지 (로그 기록 또는 무시)
			}
			throw new AuthorizationDeniedException("만료된 Refresh Token입니다. 다시 로그인해주세요.");
		}
		log.info("RefreshToken 검증을 이제 마쳤어요!!");
		// 2. RefreshToken으로부터 userId, role 추출
		Long userId = jwtProvider.extractUserId(requestDto.getRefreshToken());
		Role role = jwtProvider.extractRole(requestDto.getRefreshToken());
		log.info("RefreshToken으로 유저의 정보를 잘 찾았어요!!");
		// 3. Redis에 저장된 토큰과 일치하는지 확인
		String savedToken = refreshTokenService.get(userId);
		if (savedToken == null || !savedToken.equals(requestDto.getRefreshToken())) {
			throw new AuthorizationDeniedException("Refresh Token이 일치하지 않습니다.");
		}
		log.info("redis를 다녀왔습니다!!");
		// 4. AccessToken 재발급
		String newAccessToken = jwtProvider.createAccessToken(userId, role);
		return new TokenResponseDto(newAccessToken, requestDto.getRefreshToken());
	}

	// RDB에서 refreshToken으로 accessToken 재발급
	public TokenResponseDto refreshTokenRDBS(RefreshTokenRequestDto requestDto) {
		log.info("RDB 기반 refreshToken 메서드 실행");

		String requestToken = requestDto.getRefreshToken();

		// 1. 위변조 및 만료 체크
		if (!jwtProvider.validateToken(requestToken)) {
			Long userId = null;
			try {
				userId = jwtProvider.extractUserIdIgnoreExpiration(requestToken);
				userRepository.findById(userId).ifPresent(user -> {
					user.updateRefreshToken(null);
					userRepository.save(user);
				});
			} catch (Exception e) {
				log.warn("만료된 토큰에서 사용자 ID 추출 실패");
			}
			throw new AuthorizationDeniedException("만료된 Refresh Token입니다. 다시 로그인해주세요.");
		}

		// 2. 토큰에서 사용자 정보 추출
		Long userId = jwtProvider.extractUserId(requestToken);
		Role role = jwtProvider.extractRole(requestToken);

		// 3. DB에서 사용자 조회 및 저장된 refreshToken과 비교
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다."));

		String savedToken = user.getRefreshToken();
		if (savedToken == null || !savedToken.equals(requestToken)) {
			throw new AuthorizationDeniedException("Refresh Token이 일치하지 않습니다.");
		}

		log.info("DB에 저장된 refreshToken과 일치 확인 완료");

		// 4. AccessToken 재발급
		String newAccessToken = jwtProvider.createAccessToken(userId, role);

		return new TokenResponseDto(newAccessToken, requestToken);
	}

	public void logout(String userId) {
		refreshTokenService.delete(Long.parseLong(userId));
	}
}
