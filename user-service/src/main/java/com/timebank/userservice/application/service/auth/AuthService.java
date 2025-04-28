package com.timebank.userservice.application.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.userservice.application.dto.request.auth.LoginRequestDto;
import com.timebank.userservice.application.dto.request.auth.SignUpRequestDto;
import com.timebank.userservice.application.dto.response.auth.LoginResponseDto;
import com.timebank.userservice.application.dto.response.auth.LoginResultDto;
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
	private final AccessTokenService accessTokenService;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final PointServiceClient pointServiceClient;

	@Value("${service.admin-token}")
	private String ADMIN_TOKEN;

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
	public LoginResultDto login(LoginRequestDto requestDto) {
		User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
			() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다.")
		);
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("회원정보가 일치하지 않습니다.");
		}
		Long userId = user.getId();
		Role userRole = user.getRole();

		//토큰 생성
		String accessToken = jwtProvider.createAccessToken(userId,userRole);
		String refreshToken = jwtProvider.createRefreshToken(userId, userRole);

		accessTokenService.addToWhitelist(userId, jwtProvider.stripBearer(accessToken)); //Redis에 accessToken 저장
		refreshTokenService.save(userId, refreshToken); // Redis에 refreshToken 저장

		LoginResponseDto dto = new LoginResponseDto(user.getUsername(), accessToken, userRole);
		return new LoginResultDto(dto, refreshToken);
	}

	//리프레쉬토큰으로 새로운 accessToken발급받기
	public TokenResponseDto refreshTokenRedis(RefreshTokenRequestDto requestDto) {

		String requestToken = requestDto.getRefreshToken();

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

		// 2. RefreshToken으로부터 userId, role 추출
		Long userId = jwtProvider.extractUserId(requestDto.getRefreshToken());
		Role role = jwtProvider.extractRole(requestDto.getRefreshToken());

		// 3. Redis에 저장된 토큰과 일치하는지 확인
		String savedToken = refreshTokenService.get(userId);
		if (savedToken == null || !savedToken.equals(requestDto.getRefreshToken())) {
			throw new AuthorizationDeniedException("Refresh Token이 일치하지 않습니다.");
		}

		// 4. AccessToken 재발급
		String newAccessToken = jwtProvider.createAccessToken(userId, role);

		// 5. 새 AccessToken을 whitelist에 추가
		accessTokenService.addToWhitelist(userId, jwtProvider.stripBearer(newAccessToken));

		return new TokenResponseDto(newAccessToken, requestDto.getRefreshToken());
	}


	public void logout(Long userId, String authorizationHeader) {
		String accessToken;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			accessToken = authorizationHeader.substring(7);
		} else {
			throw new IllegalArgumentException("Invalid Authorization Header");
		}

		Long tokenUserId = jwtProvider.extractUserId(accessToken);
		if (!userId.equals(tokenUserId)) {
		   throw new AuthorizationDeniedException("Token user mismatch");
		}

		refreshTokenService.delete(userId);
		accessTokenService.addToBlacklist(accessToken);
	}
}