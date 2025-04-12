package com.timebank.gatewayservice.filter;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter {

	@Value("${service.jwt.secret-key}")
	private String secretKey;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		//회원가입과 로그인은 jwt없어도 가능해야하므로 해당 엔드포인트면 넘김
		if (path.equals("/api/v1/auth/signup") || path.equals("/api/v1/auth/login") || path.equals(
			"/api/v1/auth/refresh")) {
			return chain.filter(exchange);
		}
		log.info("signup, login, refresh가 아닌 요청이라서 이 필터에 왔어요!!");
		//헤더에서 토큰 꺼내기
		String accessToken = extractToken(exchange);
		//토큰에서 claims 꺼내기
		Claims claims = parseToken(accessToken);

		//토큰, 클레임이 null이거나 만료기간이 현재시간보다 이전이면 401에러 발생
		if (accessToken == null || claims == null || claims.getExpiration().before(new Date())) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		ServerWebExchange newExchange = createNewExchange(claims, exchange);
		return chain.filter(newExchange);
	}

	//요청헤더에서 토큰 꺼내기
	private String extractToken(ServerWebExchange exchange) {
		String token = exchange.getRequest().getHeaders().getFirst("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return null;
	}

	//토큰에서 Claims 추출하기
	private Claims parseToken(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
			Jws<Claims> claimsJws = Jwts.parser()
				.verifyWith(key)
				.build().parseSignedClaims(token);

			Claims claims = claimsJws.getPayload();

			Date expiration = claims.getExpiration();

			if (expiration != null && expiration.before(new Date())) {
				return null;
			}

			return claims;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//request에 userId, role 담아서 새론운 exchange 생성
	private ServerWebExchange createNewExchange(Claims claims, ServerWebExchange exchange) {
		ServerHttpRequest newRequest = exchange.getRequest().mutate()
			.header("X-User-Id", claims.get("user-id").toString())
			.header("X-Role", claims.get("auth").toString())
			.build();

		return exchange.mutate().request(newRequest).build();
	}
}
