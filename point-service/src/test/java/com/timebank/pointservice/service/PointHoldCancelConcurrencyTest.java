package com.timebank.pointservice.service;

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
public class PointHoldCancelConcurrencyTest {

	@Autowired
	private PointService pointService;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	@Autowired
	private PointTransactionRepository pointTransactionRepository;

	private final Long testUserId = 888L;

	@BeforeEach
	void setup() {
		pointTransactionRepository.deleteAll();
		pointAccountRepository.deleteAll();

		PointAccount account = PointAccount.builder()
			.userId(testUserId)
			.availablePoints(0)
			.holdingPoints(1000) // 1000 보류 중
			.build();

		pointAccountRepository.save(account);
	}

	@Test
	public void 동시에_여러개의_보류취소가_들어오면_정합성이_보장되어야_한다() throws InterruptedException {
		int threadCount = 30;
		int cancelAmount = 50;

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					pointService.cancelHolding(testUserId, cancelAmount, "동시성 취소 테스트");
				} catch (Exception e) {
					System.out.println("❌ 예외 발생: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		PointAccount account = pointAccountRepository.findByUserId(testUserId).orElseThrow();

		System.out.println("✅ 최종 사용 가능 포인트: " + account.getAvailablePoints());
		System.out.println("✅ 최종 보류 중 포인트: " + account.getHoldingPoints());

		assertThat(account.getAvailablePoints() + account.getHoldingPoints()).isEqualTo(1000); // 총합 보존
		assertThat(account.getAvailablePoints()).isLessThanOrEqualTo(1000); // 최대 1000까지만 복구
		assertThat(account.getHoldingPoints()).isGreaterThanOrEqualTo(0);
	}
}
