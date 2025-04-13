package com.timebank.userservice.domain.jwt;

public interface RefreshTokenRepository {
	void save(Long userId, String refreshToken);

	String get(Long userId);

	void delete(Long userId);
}
