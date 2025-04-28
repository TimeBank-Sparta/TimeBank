package com.timebank.userservice.application.dto.response.profile;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.timebank.userservice.domain.model.profile.ServiceCategory;
import com.timebank.userservice.domain.model.profile.UserLocation;
import com.timebank.userservice.domain.model.profile.UserProfile;
import com.timebank.userservice.infrastructure.client.dto.PointAccountResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMyProfileResponseDto {
	private Long id;
	private String nickname;
	private Set<ServiceCategory> helpServices;
	private Set<ServiceCategory> needServices;
	private UserLocation location;
	private String introduction;
	private Integer availablePoints;
	private Integer holdingPoints;

	public static UserMyProfileResponseDto from(UserProfile profile, PointAccountResponseDto accountResponseDto) {
		return UserMyProfileResponseDto.builder()
			.id(profile.getId())
			.nickname(profile.getNickname())
			.helpServices(
				profile.getHelpServices() != null
					? new HashSet<>(profile.getHelpServices())
					: Collections.emptySet()
			)
			.needServices(
				profile.getNeedServices() != null
					? new HashSet<>(profile.getNeedServices())
					: Collections.emptySet()
			)
			.location(profile.getLocation())
			.introduction(profile.getIntroduction())
			.availablePoints(accountResponseDto.getAvailablePoints())
			.holdingPoints(accountResponseDto.getHoldingPoints())
			.build();
	}
}
