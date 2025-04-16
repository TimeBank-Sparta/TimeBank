package com.timebank.userservice.domain.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRatingMessage {
	private Long userId;
	private Double averageRating;
}
