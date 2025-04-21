package com.timebank.common.application.dto;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T> {

	private int code;
	private String status;
	private String message;
	private T data;

	// 성공적인 응답을 위한 생성자
	public ResponseDto(int code, String status, String message, T data) {
		this.code = code;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	// 실패한 응답을 위한 생성자
	public ResponseDto(int code, String status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
		this.data = null;
	}

	public ResponseDto(HttpStatus httpStatus, String message) {
		this.code = httpStatus.value();
		this.status = httpStatus.getReasonPhrase();
		this.message = message;
		this.data = null;
	}

	// 성공 응답을 쉽게 반환할 수 있는 메서드
	public static <T> ResponseDto<T> success(T data) {
		return new ResponseDto<>(200, "success", "성공적으로 처리되었습니다.", data);
	}

	// 실패 응답을 쉽게 반환할 수 있는 메서드
	public static ResponseDto<Object> failure(HttpStatus status, String message) {
		return new ResponseDto<>(status, message);
	}

	// uri location을 담은 응답을 위한 메서드
	public static ResponseDto<Void> responseWithLocation(HttpStatus status, URI location, String message) {
		// 현재 HttpServletResponse 객체를 가져옴
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			HttpServletResponse response = requestAttributes.getResponse();
			if (response != null) {
				// Location 헤더에 URI를 추가
				response.setHeader("Location", location.toString());
			}
		}
		return new ResponseDto<>(status, message);
	}

	public static ResponseDto<Void> responseWithNoData(HttpStatus status, String message) {
		return new ResponseDto<>(status, message);
	}
}