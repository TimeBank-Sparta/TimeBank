package com.timebank.userservice.application.service.auth;

import org.springframework.stereotype.Service;

import com.timebank.userservice.domain.jwt.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.info("RefeshTokenServiceImpl의 get입니다!");
		String refreshToken = refreshTokenRepository.get(userId);
		log.info("RefreshToken : {}", refreshToken);
		return refreshToken;
	}

	@Override
	public void delete(Long userId) {
		refreshTokenRepository.delete(userId);
	}
}
