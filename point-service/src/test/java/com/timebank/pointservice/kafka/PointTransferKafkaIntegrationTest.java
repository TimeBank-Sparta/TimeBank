package com.timebank.pointservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.infrastructure.kafka.dto.PointTransferRequestMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // 캐시 클리어용
public class PointTransferKafkaIntegrationTest {

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
	void kafka_메시지_수신_후_포인트_이체가_정상_처리되어야_한다() throws Exception {
		// given
		PointTransferRequestMessage message = new PointTransferRequestMessage(
			senderUserId, receiverUserId, 200, "카프카 테스트 이체"
		);
		String jsonMessage = objectMapper.writeValueAsString(message);

		// when
		kafkaTemplate.send("points.transfer.request", jsonMessage);
		Thread.sleep(5000); // 잠시 대기: Kafka 메시지 처리 대기 (최적화 가능)

		// then
		Optional<PointAccount> sender = pointAccountRepository.findByUserId(senderUserId);
		Optional<PointAccount> receiver = pointAccountRepository.findByUserId(receiverUserId);

		assertThat(sender).isPresent();
		assertThat(receiver).isPresent();
		assertThat(sender.get().getTotalPoints()).isEqualTo(800);
		assertThat(receiver.get().getTotalPoints()).isEqualTo(700);

		System.out.println("✅ Kafka 처리 테스트 성공");
	}
}
