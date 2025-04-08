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
public class KafkaPointHoldCancelIntegrationTest {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	private final Long testUserId = 777L;
	private final String topic = "points.hold.cancel";

	@BeforeEach
	void setup() {
		pointAccountRepository.deleteAll();

		PointAccount account = PointAccount.builder()
			.userId(testUserId)
			.availablePoints(0)
			.holdingPoints(1000)
			.build();

		pointAccountRepository.save(account);
	}

	@Test
	void 동시에_Kafka_보류_취소_요청_30개_보내면_정합성이_보장되어야_한다() throws Exception {
		int threadCount = 30;
		int cancelAmount = 50;

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					PointTransferRequestMessage dto = new PointTransferRequestMessage(testUserId, null, cancelAmount, "Kafka 보류 취소 테스트");
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

		// Kafka Consumer가 처리 완료될 시간을 잠시 기다림 (1~2초)
		Thread.sleep(2000);

		PointAccount account = pointAccountRepository.findByUserId(testUserId).orElseThrow();

		System.out.println("✅ 최종 사용 가능 포인트: " + account.getAvailablePoints());
		System.out.println("✅ 최종 보류 중 포인트: " + account.getHoldingPoints());

		assertThat(account.getAvailablePoints() + account.getHoldingPoints()).isEqualTo(1000);
		assertThat(account.getAvailablePoints()).isLessThanOrEqualTo(1000);
		assertThat(account.getHoldingPoints()).isGreaterThanOrEqualTo(0);
	}
}
