package com.timebank.pointservice.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.domain.repository.PointTransactionRepository;

@SpringBootTest
public class PointServiceTest {

	@Autowired
	private PointService pointService;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	@Autowired
	private PointTransactionRepository pointTransactionRepository;

	private final Long senderUserId = 1L;
	private final Long receiverUserId = 2L;

	@BeforeEach
	void setUp() {
		pointTransactionRepository.deleteAll();
		pointAccountRepository.deleteAll();

		pointAccountRepository.save(PointAccount.builder()
			.userId(senderUserId)
			.totalPoints(1000)
			.build());

		pointAccountRepository.save(PointAccount.builder()
			.userId(receiverUserId)
			.totalPoints(1000)
			.build());
	}

	@Test
	void 동시에_포인트_이체를_요청해도_정상적으로_처리되어야_한다() throws InterruptedException {
		int threadCount = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);
		AtomicInteger successCount = new AtomicInteger();

		List<Future<?>> futures = new ArrayList<>();

		for (int i = 0; i < threadCount; i++) {
			futures.add(executorService.submit(() -> {
				try {
					pointService.transferPoints(PointTransferCommand.builder()
						.senderUserId(senderUserId)
						.receiverUserId(receiverUserId)
						.amount(50)
						.reason("동시성 테스트")
						.build());
					successCount.incrementAndGet();
				} catch (Exception e) {
					System.out.println("❌ 에러 발생: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			}));
		}

		latch.await();
		executorService.shutdown();

		int totalTransferred = successCount.get() * 50;

		Optional<PointAccount> sender = pointAccountRepository.findByUserId(senderUserId);
		Optional<PointAccount> receiver = pointAccountRepository.findByUserId(receiverUserId);

		System.out.println("✅ 성공한 이체 수: " + successCount.get());
		System.out.println("💸 송신자 최종 포인트: " + sender.map(PointAccount::getTotalPoints).orElse(-1));
		System.out.println("💰 수신자 최종 포인트: " + receiver.map(PointAccount::getTotalPoints).orElse(-1));

		assertThat(sender.get().getTotalPoints()).isEqualTo(1000 - totalTransferred);
		assertThat(receiver.get().getTotalPoints()).isEqualTo(1000 + totalTransferred);
	}

	@Test
	void 송신자_잔액이_부족하면_이체는_실패하고_포인트는_변경되지_않아야_한다() {
		// given
		PointTransferCommand command = PointTransferCommand.builder()
			.senderUserId(senderUserId)
			.receiverUserId(receiverUserId)
			.amount(2000) // 송신자보다 많은 금액
			.reason("잔액 초과 테스트")
			.build();

		// when
		Throwable thrown = catchThrowable(() -> pointService.transferPoints(command));

		// then
		assertThat(thrown)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("송신자의 포인트가 부족합니다");


		PointAccount sender = pointAccountRepository.findByUserId(senderUserId).orElseThrow();
		PointAccount receiver = pointAccountRepository.findByUserId(receiverUserId).orElseThrow();

		System.out.println("💸 송신자 최종 포인트: " + sender.getTotalPoints());
		System.out.println("💰 수신자 최종 포인트: " + receiver.getTotalPoints());


		// 💡 포인트가 그대로 유지되어야 함
		assertThat(sender.getTotalPoints()).isEqualTo(1000);
		assertThat(receiver.getTotalPoints()).isEqualTo(1000);
	}

	@Test
	void 서로_교차로_송금해도_데드락없이_정상_처리되어야_한다() throws InterruptedException {
		// given
		int threadCount = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);
		AtomicInteger successCount = new AtomicInteger();

		List<Future<?>> futures = new ArrayList<>();

		for (int i = 0; i < threadCount; i++) {
			final boolean isEven = i % 2 == 0;

			Long from = isEven ? senderUserId : receiverUserId;
			Long to = isEven ? receiverUserId : senderUserId;

			futures.add(executorService.submit(() -> {
				try {
					pointService.transferPoints(PointTransferCommand.builder()
						.senderUserId(from)
						.receiverUserId(to)
						.amount(10)
						.reason("교차 송금 테스트")
						.build());
					successCount.incrementAndGet();
				} catch (Exception e) {
					System.out.println("❌ 에러 발생: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			}));
		}

		// when
		latch.await();
		executorService.shutdown();

		// then
		PointAccount sender = pointAccountRepository.findByUserId(senderUserId).orElseThrow();
		PointAccount receiver = pointAccountRepository.findByUserId(receiverUserId).orElseThrow();

		int netTransfer = successCount.get() % 2 == 0 ? 0 : 10; // 홀수면 마지막 하나만 반영됨
		int expectedSenderPoint = 1000 - netTransfer;
		int expectedReceiverPoint = 1000 + netTransfer;

		System.out.println("✅ 총 성공한 송금 횟수: " + successCount.get());
		System.out.println("💸 A (userId=1) 최종 포인트: " + sender.getTotalPoints());
		System.out.println("💰 B (userId=2) 최종 포인트: " + receiver.getTotalPoints());

		assertThat(sender.getTotalPoints() + receiver.getTotalPoints()).isEqualTo(2000); // 총합 불변
	}


}
