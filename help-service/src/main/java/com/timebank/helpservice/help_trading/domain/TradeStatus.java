package com.timebank.helpservice.help_trading.domain;

public enum TradeStatus {
	CREATED {
		@Override
		public TradeStatus next(UserRole role) {
			if (role == UserRole.HELPER)
				return START_REQUESTED;
			throw new IllegalArgumentException("지원자만 가능합니다.");
		}
	},
	START_REQUESTED {
		@Override
		public TradeStatus next(UserRole role) {
			if (role == UserRole.REQUESTER)
				return START_APPROVED;
			throw new IllegalArgumentException("작성자만 가능합니다.");
		}

	},
	START_APPROVED {
		@Override
		public TradeStatus next(UserRole role) {
			if (role == UserRole.HELPER)
				return COMPLETE_REQUESTED;
			throw new IllegalArgumentException("지원자만 가능합니다.");
		}
	},
	COMPLETE_REQUESTED {
		@Override
		public TradeStatus next(UserRole role) {
			if (role == UserRole.REQUESTER)
				return COMPLETED;
			throw new IllegalArgumentException("작성자만 가능합니다.");
		}
	},
	COMPLETED,
	CANCELLED;

	public TradeStatus next(UserRole role) {
		throw new IllegalStateException("상태 변경 불가능");
	}
}
