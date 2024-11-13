package com.leets.xcellentbe.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leets.xcellentbe.domain.user.domain.User;

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
	private int followersCount;
	private int followingsCount;

	@JsonProperty("isFollowing")
	private Boolean isFollowing;

	@JsonProperty("isMyProfile")
	private Boolean isMyProfile;

	@Builder
	private UserProfileResponseDto(String email, String customId, String userName, String profileImageUrl,
		String backgroundProfileImageUrl, String phoneNumber, String description, String websiteUrl, String location,
		int userBirthYear, int userBirthMonth, int userBirthDay, int followersCount, int followingsCount,
		Boolean isFollowing, Boolean isMyProfile) {
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
		this.followersCount = followersCount;
		this.followingsCount = followingsCount;
		this.isMyProfile = isMyProfile;
		this.isFollowing = isFollowing;
	}

	public static UserProfileResponseDto from(User user, int followersCount, int followingsCount, Boolean isFollowing,
		Boolean isMyProfile) {
		return UserProfileResponseDto.builder()
			.email(user.getEmail())
			.customId(user.getCustomId())
			.userName(user.getUserName())
			.profileImageUrl(user.getProfileImageUrl())
			.backgroundProfileImageUrl(user.getBackgroundProfileImageUrl())
			.phoneNumber(user.getPhoneNumber())
			.description(user.getDescription())
			.websiteUrl(user.getWebsiteUrl())
			.location(user.getLocation())
			.userBirthYear(user.getUserBirth().getYear())
			.userBirthMonth(user.getUserBirth().getMonth())
			.userBirthDay(user.getUserBirth().getDay())
			.followersCount(followersCount)
			.followingsCount(followingsCount)
			.isMyProfile(isMyProfile)
			.isFollowing(isFollowing)
			.build();
	}
}
