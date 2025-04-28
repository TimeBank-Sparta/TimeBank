package com.timebank.pointservice.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.common.infrastructure.dto.PointTransferRequestMessage;
import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.application.service.PointService;

// 즉시 송금 컨슈머
@Component
public class PointTransferRequestConsumer {

	private final ObjectMapper objectMapper;
	private final PointService pointService;

	public PointTransferRequestConsumer(ObjectMapper objectMapper, PointService pointService) {
		this.objectMapper = objectMapper;
		this.pointService = pointService;
	}

	@KafkaListener(
		topics = "points.transfer.request",
		groupId = "point-service-group",
		concurrency = "4" // 🔧 단일 → 4개 Consumer Thread로 확장
	)
	public void listen(PointTransferRequestMessage message) {
		System.out.println("📩 수신 메시지: " + message);

		try {
			pointService.transferPoints(PointTransferCommand.builder()
				.senderUserId(message.getSenderUserId())
				.receiverUserId(message.getReceiverUserId())
				.amount(message.getAmount())
				.build());

			System.out.println("✅ 포인트 송금 처리 완료: " + message);
		} catch (Exception e) {
			System.err.println("❌ Kafka 메시지 처리 중 예외 발생: " + e.getMessage());
			// TODO: 재시도 or DLQ 처리
		}
	}
}
