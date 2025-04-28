package com.timebank.userservice.domain.jwt;

public interface AccessTokenRepository {
    void addToBlacklist(String accessToken, long expirationSeconds);
    void addToWhitelist(String accessToken, long expirationSeconds);

    void removeFromWhitelist(String accessToken);
}

