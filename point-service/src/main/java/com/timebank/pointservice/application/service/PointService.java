package com.timebank.pointservice.application.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.infrastructure.external.notification.dto.NotificationEvent;
import com.timebank.common.infrastructure.external.notification.dto.NotificationEventType;
import com.timebank.common.infrastructure.external.notification.dto.NotificationType;
import com.timebank.pointservice.application.dto.GetAccountResponseDto;
import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.entity.PointTransaction;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.domain.repository.PointTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

	private final PointAccountRepository pointAccountRepository;
	private final PointTransactionRepository pointTransactionRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	// 기본 토픽은 이벤트 타입에 따라 다르게 전송할 예정입니다.
	// 예를 들어, POINT_HOLD 이벤트는 NotificationEventType.CREATED.getTopic()으로 발행

	// 계정 생성
	public PointAccount createAccount(Long userId) {
		PointAccount newAccount = PointAccount.builder()
			.userId(userId)
			.availablePoints(600)
			.holdingPoints(0)
			.build();

		return pointAccountRepository.save(newAccount);
	}

	// 계정 조회
	public GetAccountResponseDto getAccount(Long userId) {
		PointAccount account = pointAccountRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));

		GetAccountResponseDto dto = new GetAccountResponseDto();
		dto.setAvailablePoints(account.getAvailablePoints());
		dto.setHoldingPoints(account.getHoldingPoints());
		return dto;
	}

	// ✅ 글 작성 시 포인트 보류
	@Transactional
	public void holdPointsForPost(Long userId, int amount) {
		PointAccount account = pointAccountRepository.findByUserIdForUpdate(userId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다."));

		// 보류 처리
		account.holdPoints(amount);

		PointTransaction holdTx = PointTransaction.builder()
			.account(account)
			.amount(-amount)
			.transactionReason("Hold: 게시글 작성")
			.build();

		pointTransactionRepository.save(holdTx);
		account.getPointTransactions().add(holdTx);

		// Kafka 이벤트 발행: 글 작성 시 포인트 보류 알림
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(userId)
			.type(NotificationType.POINT_HOLD)
			.message("글 작성으로 포인트가 보류되었습니다. (" + amount + "포인트)")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.CREATED)
			.build();
		kafkaTemplate.send(NotificationEventType.CREATED.getTopic(), event);
	}

	// ✅ 거래 확정 시 보류 포인트 → 포인트 송금 알림 (송신자와 수신자 각각 발행)
	@Transactional
	public void confirmTransfer(PointTransferCommand command) {
		Long senderUserId = command.getSenderUserId();
		Long receiverUserId = command.getReceiverUserId();
		int amount = command.getAmount();

		if (amount <= 0) {
			throw new IllegalArgumentException("보낼 포인트는 1 이상이어야 합니다.");
		}

		// 데드락을 방지하기 위해 항상 오름차순으로 락 획득
		Long firstUserId = Math.min(senderUserId, receiverUserId);
		Long secondUserId = Math.max(senderUserId, receiverUserId);

		PointAccount firstAccount = pointAccountRepository.findByUserIdForUpdate(firstUserId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + firstUserId));
		PointAccount secondAccount = pointAccountRepository.findByUserIdForUpdate(secondUserId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + secondUserId));

		PointAccount sender = senderUserId.equals(firstUserId) ? firstAccount : secondAccount;
		PointAccount receiver = receiverUserId.equals(firstUserId) ? firstAccount : secondAccount;

		// 보류 포인트 확정 및 송금 처리
		sender.confirmHolding(amount); // 보류된 포인트 차감
		receiver.increaseAvailablePoints(amount); // 수신자 포인트 증가

		PointTransaction sendTx = PointTransaction.builder()
			.account(sender)
			.amount(-amount)
			.transactionReason("Confirm: to" + receiverUserId)
			.build();
		PointTransaction receiveTx = PointTransaction.builder()
			.account(receiver)
			.amount(amount)
			.transactionReason("Receive: from" + senderUserId)
			.relatedTransaction(sendTx)
			.build();

		pointTransactionRepository.save(sendTx);
		pointTransactionRepository.save(receiveTx);
		sender.getPointTransactions().add(sendTx);
		receiver.getPointTransactions().add(receiveTx);

		// Kafka 이벤트 발행: 거래 확정 시 포인트 송금 알림
		// 송신자 알림
		NotificationEvent senderEvent = NotificationEvent.builder()
			.recipientId(senderUserId)
			.type(NotificationType.POINT_TRANSFER)
			.message("거래 확정으로 " + amount + " 포인트가 송금되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.UPDATED)
			.build();
		kafkaTemplate.send(NotificationEventType.UPDATED.getTopic(), senderEvent);

		// 수신자 알림
		NotificationEvent receiverEvent = NotificationEvent.builder()
			.recipientId(receiverUserId)
			.type(NotificationType.POINT_TRANSFER)
			.message("거래 확정으로 " + amount + " 포인트가 입금되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.UPDATED)
			.build();
		kafkaTemplate.send(NotificationEventType.UPDATED.getTopic(), receiverEvent);
	}

	// ✅ 거래 취소 시 보류 포인트 복구 알림
	@Transactional
	public void cancelHolding(Long userId, int amount) {
		PointAccount account = pointAccountRepository.findByUserIdForUpdate(userId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다."));

		account.releaseHolding(amount);
		PointTransaction cancelTx = PointTransaction.builder()
			.account(account)
			.amount(amount)
			.transactionReason("Cancel: 거래 취소")
			.build();

		pointTransactionRepository.save(cancelTx);
		account.getPointTransactions().add(cancelTx);

		// Kafka 이벤트 발행: 거래 취소 시 보류 포인트 복구 알림
		NotificationEvent event = NotificationEvent.builder()
			.recipientId(userId)
			.type(NotificationType.POINT_RECOVERY)
			.message("거래 취소로 보류된 " + amount + " 포인트가 복구되었습니다.")
			.isRead(false)
			.sentAt(LocalDateTime.now())
			.eventType(NotificationEventType.UPDATED)
			.build();
		kafkaTemplate.send(NotificationEventType.UPDATED.getTopic(), event);
	}

	// ✅ 즉시 송금 처리: 보류 없이 바로 이동 (ex. 선물하기, 즉시 결제 등)
	@Transactional
	public void transferPoints(PointTransferCommand command) {
		Long senderUserId = command.getSenderUserId();
		Long receiverUserId = command.getReceiverUserId();
		int amount = command.getAmount();

		if (amount <= 0) {
			throw new IllegalArgumentException("보낼 포인트는 1 이상이어야 합니다.");
		}

		// 항상 동일한 순서로 락 획득 → 데드락 방지
		Long firstUserId = Math.min(senderUserId, receiverUserId);
		Long secondUserId = Math.max(senderUserId, receiverUserId);

		PointAccount firstAccount = pointAccountRepository.findByUserIdForUpdate(firstUserId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + firstUserId));

		PointAccount secondAccount = pointAccountRepository.findByUserIdForUpdate(secondUserId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + secondUserId));

		PointAccount sender = senderUserId.equals(firstUserId) ? firstAccount : secondAccount;
		PointAccount receiver = receiverUserId.equals(firstUserId) ? firstAccount : secondAccount;

		// 바로 차감 및 지급
		if (sender.getAvailablePoints() < amount) {
			throw new IllegalArgumentException("송신자의 사용 가능 포인트가 부족합니다.");
		}
		sender.setAvailablePoints(sender.getAvailablePoints() - amount);
		receiver.setAvailablePoints(receiver.getAvailablePoints() + amount);

		// 거래 내역 저장
		PointTransaction sendTx = PointTransaction.builder()
			.account(sender)
			.amount(-amount)
			.transactionReason("Send: to" + receiverUserId)
			.build();

		PointTransaction receiveTx = PointTransaction.builder()
			.account(receiver)
			.amount(amount)
			.transactionReason("Receive: from" + senderUserId)
			.relatedTransaction(sendTx)
			.build();

		pointTransactionRepository.save(sendTx);
		pointTransactionRepository.save(receiveTx);

		sender.getPointTransactions().add(sendTx);
		receiver.getPointTransactions().add(receiveTx);
	}

}
