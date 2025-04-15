package com.timebank.pointservice.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HoldPointsRequestDto {
	private Long userId;
	private int amount;
}
