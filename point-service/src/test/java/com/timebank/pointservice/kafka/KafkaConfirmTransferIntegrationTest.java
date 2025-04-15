package com.timebank.pointservice.kafka;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.domain.repository.PointTransactionRepository;
import com.timebank.pointservice.infrastructure.kafka.dto.PointTransferRequestMessage;

@SpringBootTest
public class KafkaConfirmTransferIntegrationTest {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	@Autowired
	private PointTransactionRepository pointTransactionRepository;

	private final Long senderUserId = 1001L;
	private final Long receiverUserId = 1002L;
	private final String topic = "points.hold.confirm";

	@BeforeEach
	void setup() {
		pointTransactionRepository.deleteAll();
		pointAccountRepository.deleteAll();

		PointAccount sender = PointAccount.builder()
			.userId(senderUserId)
			.availablePoints(0)
			.holdingPoints(1000)
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
	void 동시에_Kafka_거래확정_요청_30개_보내면_정합성이_보장되어야_한다() throws Exception {
		int threadCount = 30;
		int transferAmount = 50; // 1000까지 전송 가능 (20개까지만 성공)

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					PointTransferRequestMessage dto = new PointTransferRequestMessage(
						senderUserId, receiverUserId, transferAmount);
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

		// Kafka Consumer가 처리할 시간 대기 (consumer는 비동기임)
		Thread.sleep(4000);

		PointAccount sender = pointAccountRepository.findByUserId(senderUserId).orElseThrow();
		PointAccount receiver = pointAccountRepository.findByUserId(receiverUserId).orElseThrow();

		int total = sender.getAvailablePoints() + sender.getHoldingPoints()
			+ receiver.getAvailablePoints() + receiver.getHoldingPoints();

		System.out.println("✅ 최종 송신자 holding: " + sender.getHoldingPoints());
		System.out.println("✅ 최종 수신자 available: " + receiver.getAvailablePoints());

		// 총 포인트 합은 보존되어야 함
		assertThat(total).isEqualTo(1000);

		// 수신자는 최대 1000 포인트까지만 받을 수 있어야 함
		assertThat(receiver.getAvailablePoints()).isLessThanOrEqualTo(1000);
	}
}
