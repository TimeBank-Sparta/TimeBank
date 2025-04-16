package com.timebank.notification_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

	// 유저 관련 알림
	USER_LOGIN("USER_LOGIN"),
	USER_INFO_UPDATE("USER_INFO_UPDATE"),
	USER_PROFILE_CREATE("USER_PROFILE_CREATE"),
	USER_PROFILE_UPDATE("USER_PROFILE_UPDATE"),

	// 포인트 관련 알림
	POINT_HOLD("POINT_HOLD"),               // 글 작성 시 포인트 보류 알림
	POINT_TRANSFER("POINT_TRANSFER"),       // 거래 확정 시 포인트 송금 알림
	POINT_RECOVERY("POINT_RECOVERY"),       // 거래 취소 시 보류 포인트 복구 알림

	// 게시글 관련 알림
	POST_CREATED("POST_CREATED"),           // 게시글 작성 알림
	POST_RECRUITMENT_COMPLETED("POST_RECRUITMENT_COMPLETED"), // 모집 완료 알림

	// 지원자 관련 알림
	APPLICANT_SUBMITTED("APPLICANT_SUBMITTED"), // 지원 완료 알림
	APPLICANT_SELECTED("APPLICANT_SELECTED"),   // 선별 완료 알림

	// 거래내역 관련 알림
	TRANSACTION_START_REQUEST("TRANSACTION_START_REQUEST"), // 거래 시작 요청
	TRANSACTION_START_COMPLETED("TRANSACTION_START_COMPLETED"), // 거래 시작 완료
	TRANSACTION_END_REQUEST("TRANSACTION_END_REQUEST"),         // 거래 종료 요청
	TRANSACTION_END_COMPLETED("TRANSACTION_END_COMPLETED");     // 거래 종료 완료

	private final String string;
}
