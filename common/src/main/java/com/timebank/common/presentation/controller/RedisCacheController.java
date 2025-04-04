package com.timebank.common.presentation.controller;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisCacheController {

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * ✅ 현재 Redis에 저장된 모든 캐시 키 조회
	 */
	@GetMapping("/keys")
	public Set<String> getAllKeys() {
		Set<String> keys = redisTemplate.keys("*");
		log.info("🔹 Redis 저장된 모든 키: {}", keys);
		return keys;
	}

	/**
	 * ✅ 특정 키의 캐시 데이터 확인
	 */
	@GetMapping("/get")
	public Object getCachedData(@RequestParam String key) {
		Object cachedValue = redisTemplate.opsForValue().get(key);
		log.info("🔹 Redis 캐시 데이터 조회: key={}, value={}", key, cachedValue);
		return cachedValue;
	}

	/**
	 * ✅ 특정 키의 캐시 삭제
	 */
	@DeleteMapping("/delete")
	public String deleteCache(@RequestParam String key) {
		redisTemplate.delete(key);
		log.info("🗑️ Redis 캐시 삭제 완료: key={}", key);
		return "✅ 캐시 삭제 완료!";
	}
}
