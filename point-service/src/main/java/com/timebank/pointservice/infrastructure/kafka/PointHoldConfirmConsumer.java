package com.timebank.pointservice.infrastructure.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.infrastructure.kafka.dto.PointTransferRequestMessage;

// 거래 확정 컨슈머
@Component
public class PointHoldConfirmConsumer {

	private final ObjectMapper objectMapper;
	private final PointService pointService;

	public PointHoldConfirmConsumer(ObjectMapper objectMapper, PointService pointService) {
		this.objectMapper = objectMapper;
		this.pointService = pointService;
	}

	@KafkaListener(
		topics = "points.hold.confirm",
		groupId = "point-service-group",
		concurrency = "4"
	)
	public void listen(ConsumerRecord<String, String> record) {
		String message = record.value();
		System.out.println("📩 [거래 확정] 수신 메시지: " + message);

		try {
			PointTransferRequestMessage dto = objectMapper.readValue(message, PointTransferRequestMessage.class);

			pointService.confirmTransfer(PointTransferCommand.builder()
				.senderUserId(dto.senderUserId())
				.receiverUserId(dto.receiverUserId())
				.amount(dto.amount())
				.build());

			System.out.println("✅ 거래 확정 포인트 이체 완료: " + dto);

		} catch (Exception e) {
			System.err.println("❌ [거래 확정] 처리 중 예외: " + e.getMessage());
		}
	}
}

