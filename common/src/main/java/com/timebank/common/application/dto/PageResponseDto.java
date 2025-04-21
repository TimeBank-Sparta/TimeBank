package com.timebank.common.application.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PageResponseDto<T> {

	private int code;
	private String status;
	private String message;
	private List data;
	private PaginationDto paginationDto;

	public PageResponseDto(HttpStatus httpStatus, Page page, String message) {
		this.code = httpStatus.value();
		this.status = httpStatus.getReasonPhrase();
		this.message = message;
		this.data = page.getContent();
		this.paginationDto = new PaginationDto(page);
	}

	public static PageResponseDto success(HttpStatus status, Page page, String message) {
		return new PageResponseDto<>(status, page, message);
	}
}