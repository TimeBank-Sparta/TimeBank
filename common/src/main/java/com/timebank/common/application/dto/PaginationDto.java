package com.timebank.common.application.dto;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class PaginationDto {
	private int page;
	private int size;
	private int totalPages;
	private long totalElements;

	/**
	 * todo: 이 부분도 builder 써야 하려나?
	 */
	public PaginationDto(Page page) {
		this.page = page.getNumber();
		this.size = page.getSize();
		this.totalPages = page.getTotalPages();
		this.totalElements = page.getTotalElements();
	}
}
