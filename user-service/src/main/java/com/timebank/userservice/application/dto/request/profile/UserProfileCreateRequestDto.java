package com.timebank.userservice.application.dto.request.profile;

import java.util.Set;

import com.timebank.userservice.domain.model.profile.ServiceCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileCreateRequestDto {
	@NotBlank
	private String nickname;

	@NotEmpty
	private Set<ServiceCategory> helpServices;

	@NotEmpty
	private Set<ServiceCategory> needServices;

	private Double latitude;

	private Double longitude;

	private String introduction;
}
