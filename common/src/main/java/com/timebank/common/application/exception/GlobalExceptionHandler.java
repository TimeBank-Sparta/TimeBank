package com.timebank.common.application.exception;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.timebank.common.application.dto.ResponseDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	private HttpHeaders headers;

	@ExceptionHandler({IllegalArgumentException.class})
	@ResponseBody
	public ResponseEntity<ResponseDto> illegalArgumentExceptionHandler(IllegalArgumentException ex,
		HttpServletRequest request) {
		log.error("IllegalArgumentException: {}", ex.getMessage());
		return handleRedirectException("IllegalArgumentException: " + ex.getMessage(), request);
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseBody
	public ResponseEntity<ResponseDto> methodArgumentNotValidExceptionHandler(
		MethodArgumentNotValidException ex, HttpServletRequest request) {
		String errorMessage = ex.getBindingResult().getAllErrors().stream()
			.reduce((first, second) -> second)
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.orElse("Validation failed");
		log.error("MethodArgumentNotValidException: {}", errorMessage);
		return handleRedirectException("MethodArgumentNotValidException: " + errorMessage, request);
	}

	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	@ResponseBody
	public ResponseEntity<ResponseDto> methodArgumentTypeMismatchExceptionHandler(
		MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
		String errorMessage = ex.getPropertyName() + " has an invalid value.";
		log.error("MethodArgumentTypeMismatchException: {}", errorMessage);
		return handleRedirectException("MethodArgumentTypeMismatchException: " + errorMessage, request);
	}

	@ExceptionHandler({NullPointerException.class})
	@ResponseBody
	public ResponseEntity<ResponseDto> nullPointerExceptionHandler(
		NullPointerException ex, HttpServletRequest request) {
		log.error("NullPointerException: {}", ex.getMessage());
		return handleRedirectException("NullPointerException: " + ex.getMessage(), request);
	}

	// EntityNotFoundException 전용 핸들러 (추가)
	@ExceptionHandler({EntityNotFoundException.class})
	@ResponseBody
	public ResponseEntity<ResponseDto> handleEntityNotFoundException(EntityNotFoundException ex) {
		log.error("EntityNotFoundException: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ResponseDto.failure(HttpStatus.NOT_FOUND, ex.getMessage()));
	}

	// 공통 리다이렉트 처리 로직
	private ResponseEntity<ResponseDto> handleRedirectException(String errorMessage, HttpServletRequest request) {
		String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
		log.info("Handling exception with message: {}", errorMessage);
		ResponseDto responseDto = new ResponseDto(
			HttpStatus.BAD_REQUEST.value(),
			HttpStatus.BAD_REQUEST.getReasonPhrase(),
			errorMessage
		);
		headers = new HttpHeaders();
		String referer = request.getHeader("Referer");
		String redirectUrl = (referer != null)
			? referer + "?error=" + encodedErrorMessage
			: "/?error=" + encodedErrorMessage;

		if (request.getRequestURI().startsWith("/api/users")) {
			redirectUrl = "/api/users/sign-up?error=" + encodedErrorMessage;
			headers.setLocation(URI.create(redirectUrl));
			return new ResponseEntity<>(responseDto, headers, HttpStatus.FOUND);
		}
		headers.setLocation(URI.create(redirectUrl));
		return new ResponseEntity<>(responseDto, headers, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CustomNotFoundException.class)
	@ResponseBody
	public ResponseEntity<ResponseDto> handleCustomNotFoundException(CustomNotFoundException e) {
		log.error("CustomNotFoundException: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ResponseDto.failure(HttpStatus.NOT_FOUND, e.getMessage()));
	}

	@ExceptionHandler(CustomForbiddenException.class)
	@ResponseBody
	public ResponseEntity<ResponseDto> handleCustomForbiddenException(CustomForbiddenException e) {
		log.error("CustomForbiddenException: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.body(ResponseDto.failure(HttpStatus.FORBIDDEN, e.getMessage()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public ResponseEntity<ResponseDto> handleConstraintViolationException(ConstraintViolationException e) {
		log.error("ConstraintViolationException: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ResponseDto.failure(HttpStatus.BAD_REQUEST, e.getMessage()));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public ResponseEntity<ResponseDto> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e) {
		log.error("HttpRequestMethodNotSupportedException: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(ResponseDto.failure(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ResponseDto> handleException(Exception e) {
		log.error("Unhandled Exception: ", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ResponseDto.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error occurred."));
	}

	@ExceptionHandler(CustomConflictException.class)
	@ResponseBody
	public ResponseEntity<ResponseDto> handleCustomConflictException(CustomConflictException e) {
		log.error("CustomConflictException: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(ResponseDto.failure(HttpStatus.CONFLICT, e.getMessage()));
	}
}