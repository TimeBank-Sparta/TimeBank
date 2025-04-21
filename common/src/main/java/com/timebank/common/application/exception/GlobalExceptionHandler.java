package com.timebank.common.application.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Order(0)
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * 처리되는 예외 목록:
	 *
	 * 1) MethodArgumentNotValidException
	 * 2) ConstraintViolationException
	 * 3) HttpRequestMethodNotSupportedException
	 * 4) HttpMessageNotReadableException
	 * 5) MissingServletRequestParameterException
	 * 6) MissingPathVariableException
	 * 7) HttpMediaTypeNotSupportedException
	 * 8) DataIntegrityViolationException
	 * 9) EntityNotFoundException
	 * 10) AccessDeniedException
	 * 11) MultipartException
	 * 12) Exception (그 외)
	 */

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse<?>> handleValidationError(MethodArgumentNotValidException ex) {
		String msg = "Validation failed: " + ex.getBindingResult().getFieldErrors();
		log.warn(msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
		String msg = "Constraint violations: " + ex.getConstraintViolations();
		log.warn(msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ExceptionResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
		String msg = ex.getMethod() + " method is not supported for this endpoint";
		log.warn(msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.METHOD_NOT_ALLOWED);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		String msg = "Malformed JSON request: " + ex.getMessage();
		log.warn(msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ExceptionResponse<?>> handleMissingRequestParam(MissingServletRequestParameterException ex) {
		String msg = "Missing request parameter: '" + ex.getParameterName() + "'";
		log.warn(msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<ExceptionResponse<?>> handleMissingPathVariable(MissingPathVariableException ex) {
		String msg = "Missing path variable: '" + ex.getVariableName() + "'";
		log.warn(msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ExceptionResponse<?>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
		String msg = "Content type '" + ex.getContentType() + "' not supported";
		log.warn(msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionResponse<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		String msg = "Database error: data integrity violation";
		log.warn(msg + ": " + ex.getMessage());
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.CONFLICT);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse<?>> handleEntityNotFound(EntityNotFoundException ex) {
		String msg = ex.getMessage();
		log.warn("Entity not found: {}", msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.NOT_FOUND);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ExceptionResponse<?>> handleAccessDenied(AccessDeniedException ex) {
		String msg = "Access denied";
		log.warn(msg + ": " + ex.getMessage());
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.FORBIDDEN);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<ExceptionResponse<?>> handleMultipart(MultipartException ex) {
		String msg = "Multipart request error: " + ex.getMessage();
		log.warn(msg);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.PAYLOAD_TOO_LARGE);
		return ResponseEntity.status(body.getStatus()).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse<?>> handleAll(Exception ex) {
		String msg = "Internal server error: " + ex.getMessage();
		log.error("Unhandled exception", ex);
		ExceptionResponse<?> body = ExceptionResponse.of(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(body.getStatus()).body(body);
	}
}
