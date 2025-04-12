package com.timebank.userservice.infrastructure.jwt;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.jwt.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.info("RefreshTokenRepositoryImpl의 get입니다.");
		String refreshToken = redisTemplate.opsForValue().get(buildKey(userId));
		log.info("RefreshToken : {}", refreshToken);
		return refreshToken;
	}

	@Override
	public void delete(Long userId) {
		redisTemplate.delete(buildKey(userId));
	}

	private String buildKey(Long userId) {
		return "refresh_token:" + userId;
	}
}
