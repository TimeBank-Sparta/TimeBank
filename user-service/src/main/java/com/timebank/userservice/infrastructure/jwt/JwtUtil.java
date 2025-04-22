package com.timebank.userservice.infrastructure.jwt;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.timebank.userservice.domain.jwt.JwtProvider;
import com.timebank.userservice.domain.model.user.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil implements JwtProvider {

	public static final String BEARER_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_KEY = "auth";
	private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;
	private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 14일

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
	@Override
	public String createAccessToken(Long userId, Role role) {
		Date now = new Date();
		return BEARER_PREFIX +
			Jwts.builder()
				.claim("user-id", userId)
				.claim(AUTHORIZATION_KEY, role)
				.expiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
				.issuedAt(now)
				.signWith(key)
				.compact();
	}

	@Override
	public String createRefreshToken(Long userId, Role role) {
		Date now = new Date();
		return Jwts.builder()
			.claim("user-id", userId)
			.claim(AUTHORIZATION_KEY, role)
			.issuedAt(now)
			.expiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
			.signWith(key)
			.compact();
	}

	@Override
	public Long extractUserId(String token) {
		Claims claims = extractAllClaims(stripBearer(token));
		return claims.get("user-id", Long.class);
	}

	@Override
	public Role extractRole(String token) {
		Claims claims = extractAllClaims(stripBearer(token));
		String roleName = claims.get(AUTHORIZATION_KEY, String.class);
		return Role.valueOf(roleName);
	}

	private Claims extractAllClaims(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			return claimsJws.getPayload();
		} catch (Exception e) {
			log.error("JWT parsing error: {}", e.getMessage());
			throw new IllegalArgumentException("Invalid token");
		}
	}

	private String stripBearer(String token) {
		return token.startsWith(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : token;
	}

	@Override
	public Long extractUserIdIgnoreExpiration(String token) {
		try {
			Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
			return claims.get("user-Id", Long.class);
		} catch (ExpiredJwtException e) {
			return e.getClaims().get("user-Id", Long.class); // 만료된 토큰에서 claims 추출
		}
	}

	@Override
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.warn("잘못된 JWT 서명입니다: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.warn("만료된 JWT 토큰입니다: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.warn("지원하지 않는 JWT 토큰입니다: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.warn("JWT 토큰이 비어있습니다: {}", e.getMessage());
		}
		return false;
	}

	@Override
	public Long getExpiration(String token) {
		Claims claims = extractAllClaims(token);
		Date expiration = claims.getExpiration();
		return expiration.getTime() - System.currentTimeMillis();
	}
}
