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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MultiUserKafkaConcurrencyTest {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	private final List<Long> userIds = List.of(1L, 2L, 3L);

	@BeforeEach
	void setUp() {
		pointAccountRepository.deleteAll();

		for (Long userId : userIds) {
			pointAccountRepository.save(PointAccount.builder()
				.userId(userId)
				.totalPoints(1000)
				.build());
		}
	}

	@Test
	void 여러_유저가_동시에_송금해도_정합성_유지되어야_한다() throws Exception {
		int transferAmount = 100;

		// 1->2, 2->3 동시에 10번씩 메시지 발송
		for (int i = 0; i < 10; i++) {
			sendTransferMessage(1L, 2L, transferAmount);
			sendTransferMessage(2L, 3L, transferAmount);
		}

		// Kafka 처리 대기
		Thread.sleep(10000);

		// 결과 확인
		PointAccount user1 = pointAccountRepository.findByUserId(1L).orElseThrow();
		PointAccount user2 = pointAccountRepository.findByUserId(2L).orElseThrow();
		PointAccount user3 = pointAccountRepository.findByUserId(3L).orElseThrow();


		System.out.println("user1: " + user1.getTotalPoints());
		System.out.println("user2: " + user2.getTotalPoints());
		System.out.println("user3: " + user3.getTotalPoints());


		assertThat(user1.getTotalPoints()).isEqualTo(1000 - (transferAmount * 10)); // 0
		assertThat(user2.getTotalPoints()).isEqualTo(1000);                        // 1000 (받고 다시 보냄)
		assertThat(user3.getTotalPoints()).isEqualTo(1000 + (transferAmount * 10)); // 2000


		System.out.println("✅ 모든 송금이 정확히 처리되었습니다.");
	}

	private void sendTransferMessage(Long sender, Long receiver, int amount) throws Exception {
		PointTransferRequestMessage message = new PointTransferRequestMessage(sender, receiver, amount, "멀티 유저 테스트");
		String json = objectMapper.writeValueAsString(message);
		kafkaTemplate.send("points.transfer.request", sender.toString(), json); // 💡 key 지정 중요
	}
}
