package com.timebank.userservice.domain.jwt;

import com.timebank.userservice.domain.model.user.Role;

public interface JwtProvider {
	String createAccessToken(Long userId, Role role);

	String createRefreshToken(Long userId, Role role);

	Long extractUserId(String token);

	Role extractRole(String token);

	Long extractUserIdIgnoreExpiration(String token);

	boolean validateToken(String token);

	Long getExpiration(String token);

	String stripBearer(String token);
}
