package com.leets.xcellentbe.domain.follow.dto;

import java.util.List;

import com.leets.xcellentbe.domain.follow.domain.Follow;
import com.leets.xcellentbe.domain.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowerAndFollowingResponseDto {
	private String customId;
	private String userName;


	@Builder
	public FollowerAndFollowingResponseDto(String customId, String userName) {
		this.customId = customId;
		this.userName = userName;
	}

	public static FollowerAndFollowingResponseDto from(Follow follow) {
		User user = follow.getFollowing();
		return FollowerAndFollowingResponseDto.builder()
			.customId(user.getCustomId())
			.userName(user.getUserName())
			.build();
	}

}
