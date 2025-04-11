package com.timebank.userservice.infrastructure.jwt;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.jwt.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
	private final StringRedisTemplate redisTemplate;
	private static final long TTL = 60 * 60 * 24 * 14; // 14일

	@Override
	public void save(Long userId, String token) {
		redisTemplate.opsForValue().set(buildKey(userId), token, TTL, TimeUnit.SECONDS);
	}

	@Override
	public String get(Long userId) {
		return redisTemplate.opsForValue().get(buildKey(userId));
	}

	@Override
	public void delete(Long userId) {
		redisTemplate.delete(buildKey(userId));
	}

	private String buildKey(Long userId) {
		return "refresh_token:" + userId;
	}
}
