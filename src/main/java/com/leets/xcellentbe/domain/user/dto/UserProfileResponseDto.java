package com.leets.xcellentbe.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserProfileResponseDto {
	private String email;
	private String customId;
	private String userName;
	private String profileImageUrl;
	private String backgroundProfileImageUrl;
	private String phoneNumber;
	private String description;
	private String websiteUrl;
	private String location;
	private int userBirthYear;
	private int userBirthMonth;
	private int userBirthDay;

	@Builder
	private UserProfileResponseDto(String email, String customId, String userName, String profileImageUrl,
		String backgroundProfileImageUrl, String phoneNumber, String description, String websiteUrl, String location,
		int userBirthYear, int userBirthMonth, int userBirthDay) {
		this.email = email;
		this.customId = customId;
		this.userName = userName;
		this.profileImageUrl = profileImageUrl;
		this.backgroundProfileImageUrl = backgroundProfileImageUrl;
		this.phoneNumber = phoneNumber;
		this.description = description;
		this.websiteUrl = websiteUrl;
		this.location = location;
		this.userBirthYear = userBirthYear;
		this.userBirthMonth = userBirthMonth;
		this.userBirthDay = userBirthDay;
	}
}
