package com.timebank.userservice.user.domain.model;

public enum Role {
	USER("USER"),
	ADMIN("ADMIN");

	private final String authority;

	Role(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}
}
