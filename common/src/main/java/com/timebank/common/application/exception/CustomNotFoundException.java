package com.timebank.common.application.exception;

public class CustomNotFoundException extends RuntimeException {
	public CustomNotFoundException(String message) {
		super(message);
	}
}