package com.timebank.common.application.exception;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse<T> {
	private String errorMessage;
	private HttpStatus status;
	private T data;
	private List<ValidationError> errors;

	// DTO 유효성 검증 오류
	public static <T> ExceptionResponse<T> of(BindingResult br) {
		return ExceptionResponse.<T>builder()
			.errorMessage("Validation Error")
			.status(HttpStatus.BAD_REQUEST)
			.errors(ValidationError.ofFieldErrors(br.getFieldErrors()))
			.build();
	}

	// 파라미터 유효성 검증 오류
	public static <T> ExceptionResponse<T> of(Set<ConstraintViolation<?>> violations) {
		return ExceptionResponse.<T>builder()
			.errorMessage("Constraint Violation")
			.status(HttpStatus.BAD_REQUEST)
			.errors(ValidationError.ofConstraintViolations(violations))
			.build();
	}

	// 단순 메시지+상태
	public static <T> ExceptionResponse<T> of(String message, HttpStatus status) {
		return ExceptionResponse.<T>builder()
			.errorMessage(message)
			.status(status)
			.build();
	}

	@Getter
	@AllArgsConstructor
	public static class ValidationError {
		private String field;
		private Object rejectedValue;
		private String reason;

		public static List<ValidationError> ofFieldErrors(List<FieldError> fieldErrors) {
			return fieldErrors.stream()
				.map(e -> new ValidationError(
					e.getField(),
					e.getRejectedValue(),
					e.getDefaultMessage()))
				.toList();
		}

		public static List<ValidationError> ofConstraintViolations(
			Set<ConstraintViolation<?>> violations) {
			return violations.stream()
				.map(v -> new ValidationError(
					v.getPropertyPath().toString(),
					v.getInvalidValue(),
					v.getMessage()))
				.toList();
		}
	}
}