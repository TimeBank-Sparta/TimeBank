package com.timebank.userservice.application.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewEvent {
	private Long revieweeId;
	private Byte rating;
	private ReviewEventType eventType;
}

