package com.timebank.pointservice.infrastructure.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.infrastructure.kafka.dto.PointTransferRequestMessage;

// 거래 취소 컨슈머
@Component
public class PointHoldCancelConsumer {

	private final ObjectMapper objectMapper;
	private final PointService pointService;

	public PointHoldCancelConsumer(ObjectMapper objectMapper, PointService pointService) {
		this.objectMapper = objectMapper;
		this.pointService = pointService;
	}

	@KafkaListener(
		topics = "points.hold.cancel",
		groupId = "point-service-group",
		concurrency = "4"
	)
	public void listen(ConsumerRecord<String, String> record) {
		String message = record.value();
		System.out.println("📩 [보류 취소] 수신 메시지: " + message);

		try {
			PointTransferRequestMessage dto = objectMapper.readValue(message, PointTransferRequestMessage.class);

			pointService.cancelHolding(dto.senderUserId(), dto.amount());

			System.out.println("✅ 포인트 보류 취소 처리 완료: " + dto);

		} catch (Exception e) {
			System.err.println("❌ [보류 취소] 처리 중 예외: " + e.getMessage());
		}
	}
}
