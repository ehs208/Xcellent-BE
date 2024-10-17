package com.leets.xcellentbe.domain.user.domain;

import java.time.LocalDateTime;
import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.leets.xcellentbe.domain.shared.BaseTimeEntity;
import com.leets.xcellentbe.domain.shared.UserStatus;
import com.leets.xcellentbe.domain.user.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

	@NotNull
	@Column
	@Enumerated(EnumType.STRING)
	private Role userRole;


	@NotNull
	@Column(length=4)
	private int userBirthYear;

	@Column(length=2)
	@NotNull
	private int userBirthMonth;

	@Column(length=2)
	@NotNull
	private int userBirthDay;

	@Builder
	private User(String customId, String email, String userName, String password, String phoneNumber, String description, int userBirthDay, int userBirthMonth, int userBirthYear, String socialEmail, Role userRole) {
		this.customId = customId;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.phoneNumber= phoneNumber;
		this.description = description;
		this.userStatus = UserStatus.ACTIVE;
		this.userRole = userRole;
		this.userBirthMonth = userBirthMonth;
		this.userBirthYear = userBirthYear;
		this.userBirthDay = userBirthDay;
	}

	public void passwordEncode(PasswordEncoder passwordEncoder) { //비밀번호 암호화 메소드
		this.password = passwordEncoder.encode(this.password);
	}

	public static User create(String customId, String email, String userName, String password, String phoneNumber, int userBirthMonth, int userBirthYear, int userBirthDay) {
		return User.builder()
			.customId(customId)
			.email(email)
			.userName(userName)
			.password(password)
			.phoneNumber(phoneNumber)
			.userBirthDay(userBirthDay)
			.userBirthYear(userBirthYear)
			.userBirthMonth(userBirthMonth)
			.userRole(Role.USER)
			.build();
	}

	public void updateProfile(String userName, String phoneNumber, String customId, int userBirthYear, int userBirthDay, int userBirthMonth, String description, String websiteUrl, String location) {
		this.userName = userName;
		this.customId = customId;
		this.description = description;
		this.websiteUrl = websiteUrl;
		this.location = location;
		this.phoneNumber = phoneNumber;
		this.userBirthYear = userBirthYear;
		this.userBirthDay = userBirthDay;
		this.userBirthMonth = userBirthMonth;
	}

	public void updateProfileImage(String updateProfileImageUrl) {
		this.profileImageUrl = updateProfileImageUrl;
	}

	public void updateBackgroundImage(String updateBackgroundImageUrl) {
		this.backgroundProfileImageUrl = updateBackgroundImageUrl;
	}

	public void updateRefreshToken(String updateRefreshToken) {
		this.refreshToken = updateRefreshToken;
	}
}
