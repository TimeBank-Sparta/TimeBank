package com.timebank.pointservice.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.pointservice.infrastructure.kafka.dto.PointTransferRequestMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPointTransferTestSender {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public KafkaPointTransferTestSender(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	public void sendTestTransfer() {
		try {
			PointTransferRequestMessage testMessage = new PointTransferRequestMessage(
				1L,      // senderAccountId
				2L,      // receiverAccountId
				100,     // amount
				"Kafka 테스트 전송"
			);

			String json = objectMapper.writeValueAsString(testMessage);

			kafkaTemplate.send(new ProducerRecord<>("points.transfer.request", json));

			System.out.println("✅ Kafka 테스트 메시지 발행 완료");
		} catch (Exception e) {
			System.err.println("❌ Kafka 테스트 메시지 발행 실패: " + e.getMessage());
		}
	}
}