package com.timebank.userservice.infrastructure.jwt;

import com.timebank.userservice.domain.jwt.AccessTokenRepository;
import com.timebank.userservice.domain.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccessTokenRepositoryImpl implements AccessTokenRepository {

    private static final String KEY_PREFIX_BLACKLIST = "blacklist:";
    private static final String KEY_PREFIX_WHITELIST = "whitelist:";

    private final StringRedisTemplate redisTemplate; // Redis 템플릿을 사용하여 Redis와 상호작용

    @Override
    public void addToBlacklist(String accessToken, long expirationSeconds) {
        try {
            redisTemplate.opsForValue().set(buildKey(KEY_PREFIX_BLACKLIST, accessToken), "true", expirationSeconds, TimeUnit.SECONDS);
            log.info("AccessToken 블랙리스트 추가 완료: {}", accessToken);
        } catch (Exception e) {
            log.error("액세스 토큰 블랙리스트 추가 중 오류 발생: {}", e.getMessage());
        }
    }

    @Override
    public void addToWhitelist(String accessToken, long expirationSeconds) {
        try {
            redisTemplate.opsForValue().set(buildKey(KEY_PREFIX_WHITELIST, accessToken), "true", expirationSeconds, TimeUnit.SECONDS);
            log.info("AccessToken 화이트리스트 추가 완료: {}", accessToken);
        } catch (Exception e) {
            log.error("액세스 토큰 화이트리스트 추가 중 오류 발생: {}", e.getMessage());
        }
    }

    @Override
    public void removeFromWhitelist(String accessToken) {
        redisTemplate.delete(buildKey(KEY_PREFIX_WHITELIST, accessToken));
    }

    private String buildKey(String prefix, String token) {
        return prefix + token;
    }
}

