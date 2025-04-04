package com.timebank.common.application.exception;

public class CustomForbiddenException extends RuntimeException {

	public CustomForbiddenException(String message) {
		super(message);
	}
}