package com.timebank.userservice.application.service.auth;

public interface RefreshTokenService {
	void save(Long userId, String refreshToken);

	String get(Long userId);

	void delete(Long userId);
}
