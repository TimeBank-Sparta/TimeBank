package com.timebank.pointservice.service;

import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.domain.repository.PointTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointServiceConfirmTransferConcurrencyTest {

	@Autowired
	private PointService pointService;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	@Autowired
	private PointTransactionRepository pointTransactionRepository;

	private final Long senderUserId = 1001L;
	private final Long receiverUserId = 1002L;

	@BeforeEach
	void setUp() {
		pointTransactionRepository.deleteAll();
		pointAccountRepository.deleteAll();

		// 송신자: 보류 포인트 1000 보유
		PointAccount sender = PointAccount.builder()
			.userId(senderUserId)
			.availablePoints(0)
			.holdingPoints(1000)
			.build();

		// 수신자: 0 포인트
		PointAccount receiver = PointAccount.builder()
			.userId(receiverUserId)
			.availablePoints(0)
			.holdingPoints(0)
			.build();

		pointAccountRepository.save(sender);
		pointAccountRepository.save(receiver);
	}

	@Test
	void 동시에_여러건의_거래확정이_들어오면_정합성이_보장되어야_한다() throws InterruptedException {
		int threadCount = 30;
		int transferAmount = 50; // 총 1000까지 성공 가능 (20건)

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					pointService.confirmTransfer(PointTransferCommand.builder()
						.senderUserId(senderUserId)
						.receiverUserId(receiverUserId)
						.amount(transferAmount)
						.reason("동시성 테스트")
						.build());
				} catch (Exception e) {
					System.out.println("❌ 예외 발생: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		PointAccount sender = pointAccountRepository.findByUserId(senderUserId).orElseThrow();
		PointAccount receiver = pointAccountRepository.findByUserId(receiverUserId).orElseThrow();

		int total = sender.getAvailablePoints() + sender.getHoldingPoints()
			+ receiver.getAvailablePoints() + receiver.getHoldingPoints();

		System.out.println("✅ 최종 송신자 holding: " + sender.getHoldingPoints());
		System.out.println("✅ 최종 수신자 available: " + receiver.getAvailablePoints());

		// ✅ 총 포인트는 유지되어야 함
		assertThat(total).isEqualTo(1000);

		// ✅ 수신자는 최대 1000포인트까지만 받을 수 있어야 함
		assertThat(receiver.getAvailablePoints()).isLessThanOrEqualTo(1000);
	}
}
