package com.leets.xcellentbe.domain.user;

import java.time.LocalDateTime;
import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.leets.xcellentbe.domain.shared.BaseTimeEntity;
import com.leets.xcellentbe.domain.shared.UserStatus;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
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
	private String userName;

	@NotNull
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
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;

	private LocalDate userBirthDay;

	@Builder
	private User(String customId, String email, String userName, String password, String phoneNumber, String description, LocalDate userBirthDay) {
		this.customId = customId;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.phoneNumber= phoneNumber;
		this.description = description;
		this.userStatus = UserStatus.ACTIVE;
		this.userBirthDay = userBirthDay;
	}

	public void passwordEncode(PasswordEncoder passwordEncoder) { //비밀번호 암호화 메소드
		this.password = passwordEncoder.encode(this.password);
	}

	public static User create(String customId, String email, String userName, String password, String phoneNumber, LocalDate userBirthDay) {
		return User.builder()
			.customId(customId)
			.email(email)
			.userName(userName)
			.password(password)
			.phoneNumber(phoneNumber)
			.userBirthDay(userBirthDay)
			.build();
	}

	public void updateRefreshToken(String updateRefreshToken) {
		this.refreshToken = updateRefreshToken;
	}
}
