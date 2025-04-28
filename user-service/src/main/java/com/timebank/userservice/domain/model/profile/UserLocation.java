package com.timebank.userservice.domain.model.profile;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserLocation {
	private double latitude;
	private double longitude;
	private String address;
}
