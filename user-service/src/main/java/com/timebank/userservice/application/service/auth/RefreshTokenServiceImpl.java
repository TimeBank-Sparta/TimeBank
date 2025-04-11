package com.timebank.userservice.application.service.auth;

import org.springframework.stereotype.Service;

import com.timebank.userservice.domain.jwt.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void save(Long userId, String refreshToken) {
		refreshTokenRepository.save(userId, refreshToken);
	}

	@Override
	public String get(Long userId) {
		return refreshTokenRepository.get(userId);
	}

	@Override
	public void delete(Long userId) {
		refreshTokenRepository.delete(userId);
	}
}
