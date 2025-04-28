package com.timebank.gatewayservice.filter;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter {

    @Value("${service.jwt.secret-key}")
    private String secretKey; // JWT 서명을 검증할 때 사용할 Secret Key

    private final ReactiveRedisTemplate<String, String> redisTemplate; // Redis 비동기 클라이언트

    // 인증 없이 접근 가능한 공개 API 경로를 정의하는 메서드
    private boolean isPublicPath(String path) {
        return path.equals("/api/v1/auth/signup") ||
                path.equals("/api/v1/auth/login") ||
                path.equals("/api/v1/auth/refresh/redis");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 공개 API는 필터링 없이 통과
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        log.info("signup, login, refresh가 아닌 요청이라서 이 필터에 왔어요!!");

        // 요청 헤더에서 Access Token 추출
        String accessToken = extractToken(exchange);

        // 토큰이 없으면 401 Unauthorized 반환
        if (accessToken == null) {
            return unauthorized(exchange, "AccessToken이 없습니다.");
        }

        // 토큰에서 Claims(유저 정보 등) 파싱
        Claims claims = parseToken(accessToken);

        // 토큰이 만료되었거나 파싱 실패 시 401 Unauthorized 반환
        if (claims == null || claims.getExpiration().before(new Date())) {
            return unauthorized(exchange, "AccessToken이 만료되었거나 잘못되었습니다.");
        }

        // 블랙리스트/화이트리스트 검증
        return validateToken(accessToken)
                .flatMap(isValid -> {
                    if (!isValid) {
                        return unauthorized(exchange, "AccessToken이 유효하지 않습니다.");
                    }

                    // 토큰 검증 완료 후, 사용자 정보를 새 요청 헤더에 담아 교체
                    ServerWebExchange newExchange = createNewExchange(claims, exchange);
                    return chain.filter(newExchange);
                });
    }

    // 요청 헤더에서 Bearer 타입 토큰 추출
    private String extractToken(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // "Bearer " 접두어 제거
        }
        return null;
    }

    // JWT 토큰 파싱하여 Claims 반환
    private Claims parseToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            Claims claims = claimsJws.getPayload();

            // 만료시간 검증
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                return null;
            }

            return claims;
        } catch (Exception e) {
            // 파싱 실패 시 null 반환
            e.printStackTrace();
        }
        return null;
    }

    // 토큰 정보를 새로운 요청 헤더에 추가하여 ServerWebExchange 갱신
    private ServerWebExchange createNewExchange(Claims claims, ServerWebExchange exchange) {
        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .header("X-User-Id", claims.get("user-id").toString()) // 사용자 ID 추가
                .header("X-Role", claims.get("auth").toString())       // 사용자 권한 추가
                .build();

        return exchange.mutate().request(newRequest).build();
    }

    // 401 Unauthorized 응답 처리
    private Mono<Void> unauthorized(ServerWebExchange exchange, String reason) {
        log.warn(reason);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    // Redis를 통해 Access Token 유효성 검증
    private Mono<Boolean> validateToken(String accessToken) {
        String blacklistKey = "blacklist:" + accessToken; // 블랙리스트 키
        String whitelistKey = "whitelist:" + accessToken; // 화이트리스트 키

        return redisTemplate.hasKey(blacklistKey)
                .flatMap(isBlacklisted -> {
                    if (Boolean.TRUE.equals(isBlacklisted)) {
                        log.warn("블랙리스트에 등록된 토큰입니다.");
                        return Mono.just(false); // 블랙리스트에 있으면 무효
                    }
                    return redisTemplate.hasKey(whitelistKey)
                            .map(isWhitelisted -> {
                                if (Boolean.FALSE.equals(isWhitelisted)) {
                                    log.warn("화이트리스트에 존재하지 않는 토큰입니다.");
                                    return false; // 화이트리스트에 없으면 무효
                                }
                                return true; // 블랙리스트X, 화이트리스트O => 유효
                            });
                });
    }
}
