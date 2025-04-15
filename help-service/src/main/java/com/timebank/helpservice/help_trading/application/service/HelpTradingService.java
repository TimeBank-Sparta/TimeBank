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
	@Transactional
	public CreateTradingResponse createHelpTrading(CreateTradingCommand command) {
		// TODO: 게시글 존재 여부 확인
		HelpTrading trading = HelpTrading.createFrom(command.toHelpTradingInfo());
		trading = helpTradingRepository.save(trading);

		// 예시: 도움 요청글 작성자에게 거래 시작 요청 알림 전송
		// ※ 아래 recipientId, notificationType, 메시지 등은 요구사항에 따라 수정 가능
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(trading.getRequesterId()) // 거래와 연관된 도움 요청글 작성자 ID (예시)
			.type(NotificationType.TRANSACTION_START_REQUEST)          // 거래 관련 알림
			.message("도움 거래가 생성되었습니다. 거래 시작 요청을 확인하세요.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.CREATED)          // CREATED 이벤트
			.build();

		// NotificationEventType.CREATED.getTopic()를 이용해 해당 이벤트 유형 전용 토픽에 발행
		kafkaTemplate.send(NotificationEventType.CREATED.getTopic(), event);
		return CreateTradingResponse.from(trading);
	}

	@Transactional
	public void createHelpTradingFromKafka(FromHelperKafkaDto command) {
		// Kafka에서 받은 데이터를 기반으로 거래 생성 (이 경우 추가적인 이벤트 발행은 생략)
		helpTradingRepository.save(HelpTrading.createFrom(command.toHelpTradingInfo()));
	}

	@Transactional(readOnly = true)
	public Page<FindHelpTradingResponse> findByHelpRequestId(Long helpRequestId, Pageable pageable) {
		return helpTradingRepository.findByHelpRequestId(helpRequestId, pageable)
			.map(FindHelpTradingResponse::from);
	}

	@Transactional
	public void delete(Long helpTradingId) {
		HelpTrading helpTrading = helpTradingRepository.findById(helpTradingId)
			.orElseThrow(() -> new CustomNotFoundException("거래내역이 없습니다."));
		helpTradingRepository.delete(helpTrading);

		// 거래 삭제 시 알림 발행 예시
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(helpTrading.getRequesterId()) // 거래와 연관된 도움 요청글 작성자 ID (예시)
			.type(NotificationType.TRANSACTION_END_REQUEST)            // 거래 관련 알림
			.message("도움 거래가 취소되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.DELETED)             // DELETED 이벤트
			.build();

		kafkaTemplate.send(NotificationEventType.DELETED.getTopic(), event);
	}
}
