package com.timebank.userservice.application.service.auth;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.timebank.userservice.domain.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessTokenBlacklistServiceImpl implements AccessTokenBlacklistService {

	private final StringRedisTemplate redisTemplate;
	private final JwtProvider jwtProvider; // AccessToken 만료 시간 확인용

	@Override
	public void addToBlacklist(String accessToken) {
		if (accessToken == null || accessToken.isEmpty()) {
			log.error("블랙리스트에 추가할 액세스 토큰이 null이거나 비어 있습니다.");
			return;
		}
		try {
			long expiration = jwtProvider.getExpiration(accessToken); // 남은 유효 시간 (ms)
			redisTemplate.opsForValue().set(buildKey(accessToken), "blacklisted", expiration, TimeUnit.MILLISECONDS);
			log.info("AccessToken 블랙리스트 추가 완료: {}", accessToken);
		} catch (Exception e) {
			log.error("액세스 토큰 블랙리스트 추가 중 오류 발생: {}", e.getMessage());
		}
	}

	@Override
	public boolean isBlacklisted(String accessToken) {
		if (accessToken == null || accessToken.isEmpty()) {
			log.warn("확인할 액세스 토큰이 null이거나 비어 있습니다.");
			return false;
		}
		return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(accessToken)));
	}

	private String buildKey(String token) {
		return "blacklist:" + token;
	}
}

