package com.timebank.userservice.application.dto.response.profile;

import java.util.Set;

import com.timebank.userservice.domain.model.profile.ServiceCategory;
import com.timebank.userservice.domain.model.profile.UserLocation;
import com.timebank.userservice.domain.model.profile.UserProfile;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponseDto {
	private Long id;
	private String nickname;
	private Set<ServiceCategory> helpServices;
	private Set<ServiceCategory> needServices;
	private UserLocation location;
	private String introduction;
	//todo: 포인트도 추가해야함

	public static UserProfileResponseDto from(UserProfile profile) {
		return UserProfileResponseDto.builder()
			.id(profile.getId())
			.nickname(profile.getNickname())
			.helpServices(profile.getHelpServices())
			.needServices(profile.getNeedServices())
			.location(profile.getLocation())
			.introduction(profile.getIntroduction())
			.build();
	}
}
