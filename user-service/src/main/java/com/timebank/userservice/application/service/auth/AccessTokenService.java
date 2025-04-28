package com.timebank.userservice.application.service.auth;

public interface AccessTokenService {
	void addToBlacklist(String accessToken);
	void addToWhitelist(Long userId, String accessToken);
}

