package com.timebank.common.application.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ExceptionResponse<T> {
	private final String errorMessage;
	private final HttpStatus status;
	private final T data;

	// 단순 메시지+상태 코드
	public static ExceptionResponse<Void> of(String message, HttpStatus status) {
		return ExceptionResponse.<Void>builder()
			.errorMessage(message)
			.status(status)
			.build();
	}
}