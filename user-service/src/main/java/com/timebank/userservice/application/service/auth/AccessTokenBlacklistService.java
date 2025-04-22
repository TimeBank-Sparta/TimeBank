package com.timebank.userservice.application.service.auth;

public interface AccessTokenBlacklistService {
	void addToBlacklist(String accessToken);

	boolean isBlacklisted(String accessToken);
}
