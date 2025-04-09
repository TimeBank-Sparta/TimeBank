package com.timebank.userservice.domain.model.profile;

public enum ServiceCategory {
	EDUCATION("교육"),
	CONSTRUCTION("공사"),
	CLEANING("청소"),
	DAILY("생활"),
	OUTING("외출"),
	CHILDCARE("육아"),
	PETCARE("반려동물"),
	ETC("기타");

	private final String description;

	ServiceCategory(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
