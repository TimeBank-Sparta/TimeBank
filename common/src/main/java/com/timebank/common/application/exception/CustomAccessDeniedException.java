package com.timebank.common.application.exception;

public class CustomAccessDeniedException extends RuntimeException {

	public CustomAccessDeniedException(String message) {
		super(message);
	}
}