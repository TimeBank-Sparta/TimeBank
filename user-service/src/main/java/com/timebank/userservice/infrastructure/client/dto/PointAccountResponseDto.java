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
	private Integer availablePoints;
	private Integer holdingPoints;
}
