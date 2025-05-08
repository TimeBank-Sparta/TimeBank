package com.timebank.helpservice.help_request.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class HelpRequestLocation {
	private String location;
	private double latitude;
	private double longitude;

	public static HelpRequestLocation createOf(String location, double latitude, double longitude) {
		return new HelpRequestLocation(location, latitude, longitude);
	}

}
