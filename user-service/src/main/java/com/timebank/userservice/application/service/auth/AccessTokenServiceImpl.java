package com.timebank.userservice.application.service.auth;

import java.util.concurrent.TimeUnit;

import com.timebank.userservice.domain.jwt.AccessTokenRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.timebank.userservice.domain.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

	private final AccessTokenRepository accessTokenRepository; // AccessTokenRepository를 의존성 주입받음
	private final JwtProvider jwtProvider;

	@Override
	public void addToBlacklist(String accessToken) {
		if (accessToken == null || accessToken.isEmpty()) {
			log.error("블랙리스트에 추가할 액세스 토큰이 null이거나 비어 있습니다.");
			return;
		}
		long expirationSeconds = calculateExpirationSeconds(accessToken);
		accessTokenRepository.addToBlacklist(accessToken, expirationSeconds);
	}

	@Override
	public void addToWhitelist(Long userId, String accessToken) {
		if (accessToken == null || accessToken.isEmpty()) {
			log.error("화이트리스트에 추가할 액세스 토큰이 null이거나 비어 있습니다.");
			return;
		}
		long expirationSeconds = calculateExpirationSeconds(accessToken);
		accessTokenRepository.addToWhitelist(accessToken, expirationSeconds);
	}

	@Override
	public void removeFromWhitelist(String accessToken) {
		if (accessToken == null || accessToken.isEmpty()) {
			log.error("화이트리스트에서 제거할 accessToken이 null이거나 비어 있습니다.");
			throw new IllegalArgumentException("accessToken must not be null or empty");
		}
		accessTokenRepository.removeFromWhitelist(accessToken);
		log.info("화이트리스트에서 accessToken 삭제 완료: {}", accessToken);
	}

	private long calculateExpirationSeconds(String accessToken) {
		long expiration = jwtProvider.getExpiration(accessToken);
		return Math.max(expiration / 1000, 1);
	}
}


