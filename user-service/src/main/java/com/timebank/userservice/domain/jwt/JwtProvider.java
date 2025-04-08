package com.timebank.userservice.domain.jwt;

import com.timebank.userservice.domain.model.user.Role;

public interface JwtProvider {
	String createAccessToken(Long userId, Role role);
}
