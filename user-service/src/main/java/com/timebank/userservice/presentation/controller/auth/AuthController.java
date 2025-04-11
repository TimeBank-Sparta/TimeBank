package com.timebank.userservice.presentation.controller.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.ResponseDto;
import com.timebank.userservice.application.dto.request.auth.LoginRequestDto;
import com.timebank.userservice.application.dto.request.auth.SignUpRequestDto;
import com.timebank.userservice.application.dto.response.auth.LoginResponseDto;
import com.timebank.userservice.application.service.auth.AuthService;
import com.timebank.userservice.presentation.dto.request.RefreshTokenRequestDto;
import com.timebank.userservice.presentation.dto.response.TokenResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody SignUpRequestDto requestDto) {
		authService.signUp(requestDto);

		Map<String, String> response = new HashMap<>();
		response.put("message", "회원가입이 완료되었습니다.");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseDto<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
		LoginResponseDto responseDto = authService.login(requestDto);
		return ResponseEntity.ok(ResponseDto.success(responseDto));
	}

	@PostMapping("/refresh")
	public ResponseEntity<ResponseDto<TokenResponseDto>> refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
		TokenResponseDto tokenResponseDto = authService.refreshToken(requestDto);
		return ResponseEntity.ok(ResponseDto.success(tokenResponseDto));
	}
}
