package com.timebank.userservice.presentation.controller.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.ResponseDto;
import com.timebank.userservice.application.dto.request.auth.LoginRequestDto;
import com.timebank.userservice.application.dto.request.auth.SignUpRequestDto;
import com.timebank.userservice.application.dto.response.auth.LoginResponseDto;
import com.timebank.userservice.application.service.auth.AuthService;
import com.timebank.userservice.presentation.dto.request.RefreshTokenRequestDto;
import com.timebank.userservice.presentation.dto.response.TokenResponseDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<ResponseDto<?>> signup(@Valid @RequestBody SignUpRequestDto requestDto) {
		authService.signUp(requestDto);

		Map<String, String> response = new HashMap<>();
		response.put("message", "회원가입이 완료되었습니다.");

		return ResponseEntity.ok(ResponseDto.success(response));

	}

	@PostMapping("/login")
	public ResponseEntity<ResponseDto<LoginResponseDto>> login(
		@Valid @RequestBody LoginRequestDto requestDto,
		HttpServletResponse response) {
		LoginResponseDto responseDto = authService.login(requestDto);

		// refresh token 쿠키 설정
		Cookie refreshTokenCookie = new Cookie("refreshToken", responseDto.getRefreshToken());
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true); // HTTPS 환경일 때만 true
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setMaxAge(14 * 24 * 60 * 60); // 7일 등

		response.addCookie(refreshTokenCookie);

		return ResponseEntity.ok(ResponseDto.success(responseDto));
	}

	@PostMapping("/refresh/redis")
	public ResponseEntity<ResponseDto<TokenResponseDto>> refreshTokenRedis(
		@RequestBody RefreshTokenRequestDto requestDto) {
		log.info("authController에서 refreshToken API를 실행!!!!!");
		TokenResponseDto tokenResponseDto = authService.refreshTokenRedis(requestDto);
		log.info("authController에서 authService.refreshToken을 무사히 마침!!!!!");
		return ResponseEntity.ok(ResponseDto.success(tokenResponseDto));
	}

	@PostMapping("/refresh/rdbs")
	public ResponseEntity<ResponseDto<TokenResponseDto>> refreshTokenRDBS(
		@RequestBody RefreshTokenRequestDto requestDto) {
		log.info("authController에서 refreshToken API를 실행!!!!!");
		TokenResponseDto tokenResponseDto = authService.refreshTokenRDBS(requestDto);
		log.info("authController에서 authService.refreshToken을 무사히 마침!!!!!");
		return ResponseEntity.ok(ResponseDto.success(tokenResponseDto));
	}

	@PostMapping("/logout")
	public ResponseEntity<ResponseDto<?>> logout(@RequestHeader("X-User-Id") String userId) {
		authService.logout(userId);

		Map<String, String> response = new HashMap<>();
		response.put("message", "로그아웃이 완료되었습니다.");

		return ResponseEntity.ok(ResponseDto.success(response));
	}
}
