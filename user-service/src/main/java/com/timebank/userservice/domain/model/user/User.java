package com.timebank.userservice.domain.model.user;

import static org.springframework.util.StringUtils.*;

import com.timebank.common.application.dto.Timestamped;
import com.timebank.userservice.domain.model.profile.UserProfile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "password")
@Table(name = "users")
@Builder(access = AccessLevel.PRIVATE, builderMethodName = "innerBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@NotNull
	@Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

	@NotNull
	@Column(name = "password", nullable = false)
	private String password;

	@NotNull
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@NotNull
	@Column(name = "phone_number", nullable = false, unique = true)
	private String phoneNumber;

	@NotNull
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private UserProfile userProfile;

	//todo : profile이랑 일대일 관계 매핑하기

	public static User of(
		String username,
		String password,
		String email,
		String phoneNumber,
		Role role
	) {
		return innerBuilder()
			.username(username)
			.password(password)
			.email(email)
			.phoneNumber(phoneNumber)
			.role(role)
			.build();
	}

	public void updateUser(String password, String email, String phoneNumber) {
		if (hasText(email))
			this.email = email;
		if (hasText(password))
			this.password = password;
		if (hasText(phoneNumber))
			this.phoneNumber = phoneNumber;
	}

}
