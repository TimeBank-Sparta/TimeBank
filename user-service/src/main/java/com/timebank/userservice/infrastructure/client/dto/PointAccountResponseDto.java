package com.timebank.userservice.infrastructure.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointAccountResponseDto {

	private Long accountId;

	private Long userId;

	private Integer availablePoints;

	private Integer holdingPoints;
}
