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
		long expiration = jwtProvider.getExpiration(accessToken); // 남은 유효 시간 (ms)
		redisTemplate.opsForValue().set(buildKey(accessToken), "blacklisted", expiration, TimeUnit.MILLISECONDS);
		log.info("AccessToken 블랙리스트 추가 완료: {}", accessToken);
	}

	@Override
	public boolean isBlacklisted(String accessToken) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(accessToken)));
	}

	private String buildKey(String token) {
		return "blacklist:" + token;
	}
}

