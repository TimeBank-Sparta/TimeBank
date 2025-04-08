package com.timebank.userservice.user.presentation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.userservice.user.application.dto.request.LoginRequestDto;
import com.timebank.userservice.user.application.dto.request.SignUpRequestDto;
import com.timebank.userservice.user.application.dto.response.LoginResponseDto;
import com.timebank.userservice.user.application.service.AuthService;

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
	public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
		LoginResponseDto responseDto = authService.login(requestDto);
		return ResponseEntity.ok(responseDto);
	}
}
