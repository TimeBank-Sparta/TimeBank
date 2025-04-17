package com.timebank.pointservice.application.dto;

import lombok.Data;

@Data
public class GetAccountResponseDto {
	Integer availablePoints;
	Integer holdingPoints;
}
