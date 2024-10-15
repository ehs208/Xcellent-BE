package com.leets.xcellentbe.global.auth.login.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

import com.leets.xcellentbe.domain.user.Role;
import com.leets.xcellentbe.domain.user.domain.User;

@Getter
public class OAuthAttributes {

	private final String nameAttributeKey;
	private final GoogleOAuthUserInfo googleOAuthUserInfo;
	private final PasswordUtil passwordUtil;

	@Builder
	private OAuthAttributes(String nameAttributeKey, GoogleOAuthUserInfo googleOAuthUserInfo, PasswordUtil passwordUtil) {
		this.nameAttributeKey = nameAttributeKey;
		this.googleOAuthUserInfo = googleOAuthUserInfo;
		this.passwordUtil = passwordUtil;
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
			.password(passwordUtil.generateHashedRandomPassword())
			.customId("customId")
			.userName("userName")
			.userRole(Role.GUEST)
			.build();
	}
}
