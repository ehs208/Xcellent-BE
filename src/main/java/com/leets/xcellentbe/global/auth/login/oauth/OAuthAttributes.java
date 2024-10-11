package com.leets.xcellentbe.global.auth.login.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

import com.leets.xcellentbe.domain.user.Role;
import com.leets.xcellentbe.domain.user.domain.User;

@Getter
public class OAuthAttributes {

	private String nameAttributeKey;
	private GoogleOAuthUserInfo googleOAuthUserInfo;

	@Builder
	private OAuthAttributes(String nameAttributeKey, GoogleOAuthUserInfo googleOAuthUserInfo) {
		this.nameAttributeKey = nameAttributeKey;
		this.googleOAuthUserInfo = googleOAuthUserInfo;
	}

	public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.googleOAuthUserInfo(new GoogleOAuthUserInfo(attributes))
			.build();
	}

	public User toEntity(GoogleOAuthUserInfo googleOAuthUserInfo) {
		return User.builder()
			.email(googleOAuthUserInfo.getEmail())
			.password("password")
			.customId("customId")
			.userName("userName")
			.userRole(Role.GUEST)
			.build();
	}
}
