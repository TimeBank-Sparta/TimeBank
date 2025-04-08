package com.timebank.pointservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.infrastructure.kafka.dto.PointTransferRequestMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class KafkaPointTransferIntegrationTest {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	private final Long senderUserId = 999L;
	private final Long receiverUserId = 1000L;
	private final String topic = "points.transfer.request";

	@BeforeEach
	void setup() {
		pointAccountRepository.deleteAll();

		PointAccount sender = PointAccount.builder()
			.userId(senderUserId)
			.availablePoints(1000)  // 송금 가능한 포인트
			.holdingPoints(0)
			.build();

		PointAccount receiver = PointAccount.builder()
			.userId(receiverUserId)
			.availablePoints(0)
			.holdingPoints(0)
			.build();

		pointAccountRepository.save(sender);
		pointAccountRepository.save(receiver);
	}

	@Test
	void 동시에_Kafka_송금요청_30개를_보내면_정합성이_보장되어야_한다() throws Exception {
		int threadCount = 30;
		int transferAmount = 50;

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					PointTransferRequestMessage dto = new PointTransferRequestMessage(
						senderUserId, receiverUserId, transferAmount, "Kafka 송금 테스트");

					String message = objectMapper.writeValueAsString(dto);
					kafkaTemplate.send(new ProducerRecord<>(topic, message));
				} catch (Exception e) {
					System.out.println("❌ Kafka 메시지 전송 중 예외: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// Kafka Consumer가 처리 완료될 시간을 잠시 기다림
		Thread.sleep(2000);

		PointAccount sender = pointAccountRepository.findByUserId(senderUserId).orElseThrow();
		PointAccount receiver = pointAccountRepository.findByUserId(receiverUserId).orElseThrow();

		System.out.println("✅ 최종 송신자 사용 가능 포인트: " + sender.getAvailablePoints());
		System.out.println("✅ 최종 수신자 사용 가능 포인트: " + receiver.getAvailablePoints());

		int totalTransferred = receiver.getAvailablePoints();
		int totalRemaining = sender.getAvailablePoints();
		int total = totalTransferred + totalRemaining;

		// ✅ 총 포인트는 보존되어야 함
		assertThat(total).isEqualTo(1000);

		// ✅ 수신자는 최대 1000포인트까지만 받을 수 있어야 함
		assertThat(receiver.getAvailablePoints()).isLessThanOrEqualTo(1000);

		// ✅ 송신자는 0 이상이어야 함 (동시성 실패로 일부 트랜잭션 무시될 수 있음)
		assertThat(sender.getAvailablePoints()).isGreaterThanOrEqualTo(0);
	}
}
