package com.timebank.userservice.application.dto.request.profile;

import java.util.Set;

import com.timebank.userservice.domain.model.profile.ServiceCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequestDto {
	private String nickname;
	private Set<ServiceCategory> helpServices;
	private Set<ServiceCategory> needServices;
	private String location;
	private String introduction;
}
