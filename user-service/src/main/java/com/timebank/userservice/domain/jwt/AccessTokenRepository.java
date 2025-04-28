package com.timebank.userservice.domain.jwt;

public interface AccessTokenRepository {
    void addToBlacklist(String accessToken, long expirationSeconds);
    boolean isBlacklisted(String accessToken);
    void addToWhitelist(Long userId, String accessToken, long expirationSeconds);
}

