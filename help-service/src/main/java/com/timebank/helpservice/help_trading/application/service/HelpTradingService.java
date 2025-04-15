package com.timebank.helpservice.help_trading.application.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.application.exception.CustomNotFoundException;
import com.timebank.common.infrastructure.external.notification.dto.NotificationEvent;
import com.timebank.common.infrastructure.external.notification.dto.NotificationEventType;
import com.timebank.common.infrastructure.external.notification.dto.NotificationType;
import com.timebank.helpservice.help_trading.application.dto.request.CreateTradingCommand;
import com.timebank.helpservice.help_trading.application.dto.response.CreateTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.FindHelpTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.FromHelperKafkaDto;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;
import com.timebank.helpservice.help_trading.infrastructure.db.JpaHelpRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HelpTradingService {

	private final JpaHelpRepository helpTradingRepository;
	private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

	// 거래 생성 시 알림 발행 예시

	/**
	 * 도움 거래 생성 (도움 요청 글 종료 후 거래 시작)
	 */
	@Transactional
	public CreateTradingResponse createHelpTrading(CreateTradingCommand command) {
		// TODO: 게시글 존재 여부 확인
		HelpTrading trading = HelpTrading.createFrom(command.toHelpTradingInfo());
		trading = helpTradingRepository.save(trading);

		// 거래 생성 시, 도움 요청 글 작성자(요청자)에게 거래 시작 요청 알림 발행
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(trading.getRequesterId())  // 거래와 연결된 도움 요청 글 작성자 ID
			.type(NotificationType.TRANSACTION_START_REQUEST)
			.message("도움 거래가 생성되었습니다. 거래 시작 요청을 확인하세요.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.CREATED)
			.build();
		kafkaTemplate.send(NotificationEventType.CREATED.getTopic(), event);

		return CreateTradingResponse.from(trading);
	}

	/**
	 * Kafka에서 받은 데이터 기반 도움 거래 생성 (지원자 관련)
	 */
	@Transactional
	public void createHelpTradingFromKafka(FromHelperKafkaDto command) {
		helpTradingRepository.save(HelpTrading.createFrom(command.toHelpTradingInfo()));
	}

	/**
	 * 도움 요청 글에 따른 거래 내역 조회
	 */
	@Transactional(readOnly = true)
	public Page<FindHelpTradingResponse> findByHelpRequestId(Long helpRequestId, Pageable pageable) {
		return helpTradingRepository.findByHelpRequestId(helpRequestId, pageable)
			.map(FindHelpTradingResponse::from);
	}

	/**
	 * 도움 거래 삭제 (취소 시)
	 */
	@Transactional
	public void delete(Long helpTradingId) {
		HelpTrading helpTrading = helpTradingRepository.findById(helpTradingId)
			.orElseThrow(() -> new CustomNotFoundException("거래내역이 없습니다."));
		helpTradingRepository.delete(helpTrading);

		// 거래 취소 시 알림 이벤트 발행: 거래 종료 요청 (취소)
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(helpTrading.getRequesterId()) // 거래와 연결된 도움 요청 글 작성자 ID
			.type(NotificationType.TRANSACTION_END_REQUEST)
			.message("도움 거래가 취소되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.DELETED)
			.build();
		kafkaTemplate.send(NotificationEventType.DELETED.getTopic(), event);
	}

}
