package com.timebank.pointservice.domain.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "point_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Integer availablePoints; // 사용 가능한 포인트

	@Column(nullable = false)
	private Integer holdingPoints;   // 보류 중인 포인트

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<PointTransaction> pointTransactions;

	// ✅ 포인트 차감 (글 작성 시)
	public void holdPoints(int amount) {
		if (this.availablePoints < amount) {
			throw new IllegalArgumentException("사용 가능한 포인트가 부족합니다.");
		}
		this.availablePoints -= amount;
		this.holdingPoints += amount;
	}

	// ✅ 거래 성사: 보류 → 상대방으로 전송
	public void confirmHolding(int amount) {
		if (this.holdingPoints < amount) {
			throw new IllegalArgumentException("보류 중인 포인트가 부족합니다.");
		}
		this.holdingPoints -= amount;
		// 상대방 계좌로 increase는 외부에서 처리
	}

	// ✅ 거래 취소: 보류 → 다시 사용 가능으로 복귀
	public void releaseHolding(int amount) {
		if (this.holdingPoints < amount) {
			throw new IllegalArgumentException("보류 중인 포인트가 부족합니다.");
		}
		this.holdingPoints -= amount;
		this.availablePoints += amount;
	}

	// ✅ 일반 지급 (상대방이 포인트 받는 경우)
	public void increaseAvailablePoints(int amount) {
		this.availablePoints += amount;
	}
}
