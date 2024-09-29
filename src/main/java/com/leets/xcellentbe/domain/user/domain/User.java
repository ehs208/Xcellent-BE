package com.leets.xcellentbe.domain.user.domain;

import java.time.LocalDateTime;

import com.leets.xcellentbe.domain.shared.BaseTimeEntity;
import com.leets.xcellentbe.domain.shared.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotNull
	@Column
	private String customId;

	@NotNull
	@Column(length = 100)
	private String username;

	private String password;

	@Column
	private String profileImageUrl;

	@Column
	private String backgroundProfileImageUrl;

	private String refreshToken;

	@NotNull
	@Column(length = 50)
	private String email;

	@Column(length = 20)
	private String phoneNumber;

	@Column
	private LocalDateTime inactiveDate;

	@Column
	private String description;

	@Column
	private String websiteUrl;

	@Column(length = 100)
	private String location;

	@NotNull
	@Column
	private UserStatus userStatus;
}
