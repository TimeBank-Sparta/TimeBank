package com.timebank.userservice.application.dto.request.profile;

import java.util.Set;

import com.timebank.userservice.domain.model.profile.ServiceCategory;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequestDto {
	@Size(min = 2, max = 20, message = "닉네임은 2~20자 이내여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 특수문자를 포함할 수 없습니다.")
	private String nickname;

	@Size(max = 10, message = "최대 10개의 서비스만 선택할 수 있습니다.")
	private Set<ServiceCategory> helpServices;

	@Size(max = 10, message = "최대 10개의 서비스만 선택할 수 있습니다.")
	private Set<ServiceCategory> needServices;

	@DecimalMin(value = "-90.0", message = "위도는 -90~90 사이여야 합니다.")
	@DecimalMax(value = "90.0", message = "위도는 -90~90 사이여야 합니다.")
	private Double latitude;

	@DecimalMin(value = "-180.0", message = "경도는 -180~180 사이여야 합니다.")
	@DecimalMax(value = "180.0", message = "경도는 -180~180 사이여야 합니다.")
	private Double longitude;

	@Size(max = 500, message = "자기소개는 500자 이내여야 합니다.")
	private String introduction;
}
