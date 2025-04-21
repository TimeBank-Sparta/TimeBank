package com.timebank.common.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	// DTO 유효성 검증 실패
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse<?>> handleValidationError(
		MethodArgumentNotValidException ex) {

		log.warn("GlobalExceptionHandler : Validation failed: {}", ex.getBindingResult().getFieldErrors());
		ExceptionResponse<?> body = ExceptionResponse.of(ex.getBindingResult());
		return ResponseEntity
			.status(body.getStatus())
			.body(body);
	}

	// @RequestParam, @PathVariable 등 parameter 검증 실패
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse<?>> handleConstraintViolation(
		ConstraintViolationException ex) {

		log.warn("GlobalExceptionHandler : Constraint violations: {}", ex.getConstraintViolations());
		ExceptionResponse<?> body = ExceptionResponse.of(ex.getConstraintViolations());
		return ResponseEntity
			.status(body.getStatus())
			.body(body);
	}

	// 지원되지 않는 HTTP method
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ExceptionResponse<?>> handleMethodNotAllowed(
		HttpRequestMethodNotSupportedException ex) {

		String msg = "GlobalExceptionHandler : " + ex.getMethod() + " method is not supported for this endpoint";
		log.warn(msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.METHOD_NOT_ALLOWED);
		return ResponseEntity
			.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(body);
	}

	// 엔티티를 찾지 못했을 때
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse<?>> handleNotFound(EntityNotFoundException ex) {
		log.warn("GlobalExceptionHandler : EntityNotFound: {}", ex.getMessage());
		ExceptionResponse<?> body = ExceptionResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND);
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(body);
	}

	// 잘못된 인자 전달 등
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ExceptionResponse<?>> handleBadRequest(IllegalArgumentException ex) {
		log.warn("GlobalExceptionHandler : IllegalArgument: {}", ex.getMessage());
		ExceptionResponse<?> body = ExceptionResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(body);
	}

	// 그 외 모든 예외
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse<?>> handleAll(Exception ex) {
		log.error("GlobalExceptionHandler : Unhandled exception", ex);
		ExceptionResponse<?> body = ExceptionResponse.of(
			"Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(body);
	}
}
