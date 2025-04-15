package com.timebank.pointservice.infrastructure.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.infrastructure.kafka.dto.PointTransferRequestMessage;

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
	public void listen(ConsumerRecord<String, String> record) {
		String message = record.value();
		System.out.println("📩 수신 메시지: " + message);

		try {
			PointTransferRequestMessage dto = objectMapper.readValue(message, PointTransferRequestMessage.class);

			pointService.transferPoints(PointTransferCommand.builder()
				.senderUserId(dto.senderUserId())
				.receiverUserId(dto.receiverUserId())
				.amount(dto.amount())
				.build());

			System.out.println("✅ 포인트 송금 처리 완료: " + dto);

		} catch (com.fasterxml.jackson.core.JsonProcessingException e) {
			System.err.println("❌ JSON 파싱 실패: " + e.getMessage());
			// TODO: 추후 InvalidMessage DLQ 토픽으로 이동
		} catch (Exception e) {
			System.err.println("❌ Kafka 메시지 처리 중 예외 발생: " + e.getMessage());
			// TODO: 재시도 or DLQ 처리
		}
	}
}
