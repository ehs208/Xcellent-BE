package com.leets.xcellentbe.domain.follow.dto;

import com.leets.xcellentbe.domain.follow.domain.Follow;
import com.leets.xcellentbe.domain.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowInfoResponseDto {
	private String customId;
	private String userName;

	@Builder
	public FollowInfoResponseDto(String customId, String userName) {
		this.customId = customId;
		this.userName = userName;
	}

	public static FollowInfoResponseDto from(Follow follow) {
		User user = follow.getFollowing();
		return FollowInfoResponseDto.builder()
			.customId(user.getCustomId())
			.userName(user.getUserName())
			.build();
	}

}
