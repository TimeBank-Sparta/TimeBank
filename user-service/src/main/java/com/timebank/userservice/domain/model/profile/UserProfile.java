package com.timebank.userservice.domain.model.profile;

import java.util.HashSet;
import java.util.Set;

import com.timebank.common.domain.Timestamped;
import com.timebank.userservice.domain.model.user.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_profiles")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE, builderMethodName = "innerBuilder")
public class UserProfile extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_profile_id")
	private Long id;

	// 유저 연관관계
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull
	@Column(name = "nickname", nullable = false, unique = true, length = 10)
	private String nickname;

	// 도움 줄 수 있는 서비스
	@ElementCollection(targetClass = ServiceCategory.class)
	@CollectionTable(name = "user_help_services", joinColumns = @JoinColumn(name = "user_profile_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "help_service")
	@Builder.Default
	private Set<ServiceCategory> helpServices = new HashSet<>();

	// 도움 받을 서비스
	@ElementCollection(targetClass = ServiceCategory.class)
	@CollectionTable(name = "user_need_services", joinColumns = @JoinColumn(name = "user_profile_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "need_service")
	@Builder.Default
	private Set<ServiceCategory> needServices = new HashSet<>();

	@Embedded
	private UserLocation location;

	private String introduction;

	private Double trustScore;

	private int reviewCount;

	public static UserProfile of(
		User user,
		String nickname,
		Set<ServiceCategory> helpServices,
		Set<ServiceCategory> needServices,
		UserLocation location,
		String introduction,
		Double trustScore,
		int reviewCount
	) {
		return innerBuilder()
			.user(user)
			.nickname(nickname)
			.helpServices(helpServices)
			.needServices(needServices)
			.location(location)
			.introduction(introduction)
			.trustScore(trustScore)
			.reviewCount(reviewCount)
			.build();
	}

	public void update(
		String nickname,
		Set<ServiceCategory> helpServices,
		Set<ServiceCategory> needServices,
		UserLocation location,
		String introduction) {
		if (nickname != null) {
			this.nickname = nickname;
		}
		if (helpServices != null) {
			this.helpServices = helpServices;
		}
		if (needServices != null) {
			this.needServices = needServices;
		}
		if (location != null) {
			this.location = location;
		}
		if (introduction != null) {
			this.introduction = introduction;
		}
	}

	public void updateRating(Double averageRating) {
		this.trustScore = averageRating;
	}

	public void updateReviewCount(int count) {
		this.reviewCount = count;
	}
}
