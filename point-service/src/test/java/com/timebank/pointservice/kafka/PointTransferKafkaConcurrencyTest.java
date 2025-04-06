package com.timebank.pointservice.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.infrastructure.kafka.dto.PointTransferRequestMessage;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PointTransferKafkaConcurrencyTest {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	private final Long senderUserId = 1L;
	private final Long receiverUserId = 2L;

	@BeforeEach
	void setUp() {
		pointAccountRepository.deleteAll();

		pointAccountRepository.save(PointAccount.builder()
			.userId(senderUserId)
			.totalPoints(1000)
			.build());

		pointAccountRepository.save(PointAccount.builder()
			.userId(receiverUserId)
			.totalPoints(500)
			.build());
	}

	@Test
	void 동시에_여러_Kafka_메시지를_보내도_정합성_유지되어야_한다() throws Exception {
		int messageCount = 10; // 보낼 메시지 수
		int amountPerTransfer = 50;

		// 💌 Kafka 메시지 발행
		for (int i = 0; i < messageCount; i++) {
			PointTransferRequestMessage message = new PointTransferRequestMessage(
				senderUserId, receiverUserId, amountPerTransfer, "동시성 테스트"
			);
			String json = objectMapper.writeValueAsString(message);
			kafkaTemplate.send("points.transfer.request", senderUserId.toString(), json); // 🔧 여기 수정
		}

		// 💤 Kafka Consumer 처리 시간 대기
		Thread.sleep(10000); // 필요시 5~10초로 조정

		// 📊 결과 조회
		PointAccount sender = pointAccountRepository.findByUserId(senderUserId).orElseThrow();
		PointAccount receiver = pointAccountRepository.findByUserId(receiverUserId).orElseThrow();

		int totalTransferred = messageCount * amountPerTransfer;

		// ✅ 정합성 검증
		assertThat(sender.getTotalPoints()).isEqualTo(1000 - totalTransferred);
		assertThat(receiver.getTotalPoints()).isEqualTo(500 + totalTransferred);

		System.out.println("✅ 최종 송신자 포인트: " + sender.getTotalPoints());
		System.out.println("✅ 최종 수신자 포인트: " + receiver.getTotalPoints());
	}

}

