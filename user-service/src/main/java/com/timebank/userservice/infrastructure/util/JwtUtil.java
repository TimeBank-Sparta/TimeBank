package com.timebank.userservice.infrastructure.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.timebank.userservice.domain.model.user.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_KEY = "auth";
	private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;

	@Value("${service.jwt.secret-key}")
	private String secretKey;
	private SecretKey key;

	@PostConstruct // 객체가 생성되고 의존성 주입이 완료된 후 자동으로 실행되는 메서드
	public void init() {
		// Base64로 인코딩된 secretKey를 디코딩하여 바이트 배열로 변환
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		// 변환된 바이트 배열을 사용하여 HMAC-SHA 키 객체 생성
		key = Keys.hmacShaKeyFor(bytes);
	}

	//토큰 생성
	public String createAccessToken(Long userId, Role role) {
		Date now = new Date();
		return BEARER_PREFIX +
			Jwts.builder()
				.claim("user_id", userId)
				.claim(AUTHORIZATION_KEY, role)
				.expiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
				.issuedAt(now)
				.signWith(key)
				.compact();
	}
}
