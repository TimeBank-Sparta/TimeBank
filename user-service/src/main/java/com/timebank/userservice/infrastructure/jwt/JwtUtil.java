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

	// 토큰에 사용할 상수들
	public static final String BEARER_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_KEY = "auth";

	// 액세스 토큰 및 리프레시 토큰의 유효 기간 (밀리초 단위)
	private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 1시간
	private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 14일

	// secretKey는 외부 설정에서 가져오며, 이를 바탕으로 HMAC SHA-256 키를 생성
	@Value("${service.jwt.secret-key}")
	private String secretKey;
	private SecretKey key;

	// @PostConstruct: 객체가 생성된 후 의존성 주입이 완료되면 자동으로 실행
	@PostConstruct
	public void init() {
		// Base64로 인코딩된 secretKey를 디코딩하여 바이트 배열로 변환
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		// 바이트 배열을 사용하여 HMAC-SHA 키 객체 생성
		key = Keys.hmacShaKeyFor(bytes);
	}

	/**
	 * 액세스 토큰을 생성하는 메서드.
	 * @param userId 사용자 ID
	 * @param role 사용자 권한(Role)
	 * @return 생성된 액세스 토큰
	 */
	@Override
	public String createAccessToken(Long userId, Role role) {
		Date now = new Date(); // 현재 시간
		return BEARER_PREFIX + // JWT 토큰 앞에 "Bearer "를 붙임
				Jwts.builder()
						.claim("user-id", userId) // 사용자 ID를 클레임에 포함
						.claim(AUTHORIZATION_KEY, role) // 사용자 권한을 클레임에 포함
						.expiration(new Date(now.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간 설정
						.issuedAt(now) // 발행 시간 설정
						.signWith(key) // HMAC-SHA로 서명
						.compact(); // JWT 토큰 문자열 생성
	}

	/**
	 * 리프레시 토큰을 생성하는 메서드.
	 * @param userId 사용자 ID
	 * @param role 사용자 권한(Role)
	 * @return 생성된 리프레시 토큰
	 */
	@Override
	public String createRefreshToken(Long userId, Role role) {
		Date now = new Date(); // 현재 시간
		return Jwts.builder()
				.claim("user-id", userId) // 사용자 ID를 클레임에 포함
				.claim(AUTHORIZATION_KEY, role) // 사용자 권한을 클레임에 포함
				.issuedAt(now) // 발행 시간 설정
				.expiration(new Date(now.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간 설정
				.signWith(key) // HMAC-SHA로 서명
				.compact(); // JWT 토큰 문자열 생성
	}

	/**
	 * 토큰에서 사용자 ID를 추출하는 메서드.
	 * @param token JWT 토큰
	 * @return 사용자 ID
	 */
	@Override
	public Long extractUserId(String token) {
		Claims claims = extractAllClaims(stripBearer(token)); // Bearer 부분을 제거한 후 Claims 추출
		return claims.get("user-id", Long.class); // 사용자 ID 추출
	}

	/**
	 * 토큰에서 사용자 권한(Role)을 추출하는 메서드.
	 * @param token JWT 토큰
	 * @return 사용자 권한(Role)
	 */
	@Override
	public Role extractRole(String token) {
		Claims claims = extractAllClaims(stripBearer(token)); // Bearer 부분을 제거한 후 Claims 추출
		String roleName = claims.get(AUTHORIZATION_KEY, String.class); // 권한 이름 추출
		return Role.valueOf(roleName); // Role enum으로 변환하여 반환
	}

	/**
	 * 토큰에서 만료된 JWT의 경우도 사용자 ID를 추출할 수 있도록 처리.
	 * @param token JWT 토큰
	 * @return 사용자 ID
	 */
	@Override
	public Long extractUserIdIgnoreExpiration(String token) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(key) // 서명 검증
					.build()
					.parseSignedClaims(token)
					.getPayload();
			return claims.get("user-Id", Long.class); // 사용자 ID 추출
		} catch (ExpiredJwtException e) {
			return e.getClaims().get("user-id", Long.class); // 만료된 토큰에서 사용자 ID 추출
		}
	}

	/**
	 * JWT 토큰이 유효한지 검증하는 메서드.
	 * @param token JWT 토큰
	 * @return 유효성 검사 결과 (true: 유효, false: 유효하지 않음)
	 */
	@Override
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.verifyWith(key) // 서명 검증
					.build()
					.parseSignedClaims(token); // JWT 파싱
			return true; // 유효한 토큰
		} catch (SecurityException | MalformedJwtException e) {
			log.warn("잘못된 JWT 서명입니다: {}", e.getMessage()); // 서명 오류
		} catch (ExpiredJwtException e) {
			log.warn("만료된 JWT 토큰입니다: {}", e.getMessage()); // 만료된 토큰 오류
		} catch (UnsupportedJwtException e) {
			log.warn("지원하지 않는 JWT 토큰입니다: {}", e.getMessage()); // 지원하지 않는 토큰 형식
		} catch (IllegalArgumentException e) {
			log.warn("JWT 토큰이 비어있습니다: {}", e.getMessage()); // 비어있는 토큰 오류
		}
		return false; // 유효하지 않은 토큰
	}

	/**
	 * JWT 토큰의 만료 시간을 가져오는 메서드.
	 * @param token JWT 토큰
	 * @return 남은 유효 시간 (밀리초 단위)
	 */
	@Override
	public Long getExpiration(String token) {
		Claims claims = extractAllClaims(stripBearer(token)); // JWT 토큰에서 Claims 추출
		Date expiration = claims.getExpiration(); // 만료 시간 추출
		Long diff = expiration.getTime() - System.currentTimeMillis(); // 남은 시간 계산
		return Math.max(diff, 0); // 최소 0초를 보장
	}

	/**
	 * JWT 토큰에서 'Bearer ' 부분을 제거하는 메서드.
	 * @param token JWT 토큰
	 * @return 'Bearer '가 제거된 JWT 토큰
	 */
	@Override
	public String stripBearer(String token) {
		return token.startsWith(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : token;
	}

	/**
	 * JWT에서 Claims을 추출하는 내부 메서드.
	 * @param token JWT 토큰
	 * @return JWT Claims
	 */
	private Claims extractAllClaims(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser()
					.verifyWith(key) // 서명 검증
					.build()
					.parseSignedClaims(token); // JWT 파싱
			return claimsJws.getPayload(); // Claims 반환
		} catch (Exception e) {
			log.error("JWT parsing error: {}", e.getMessage()); // 파싱 오류 로깅
			throw new IllegalArgumentException("Invalid token"); // 유효하지 않은 토큰 예외 발생
		}
	}
}