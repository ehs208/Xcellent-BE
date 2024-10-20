package com.leets.xcellentbe.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter

public class UserProfileRequestDto {
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
}
