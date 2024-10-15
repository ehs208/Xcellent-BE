package com.leets.xcellentbe.global.auth.login.oauth;

import java.util.Map;

public class GoogleOAuthUserInfo {

	private final Map<String, Object> attributes;

	public GoogleOAuthUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public String getEmail() {
		return (String) attributes.get("email");
	}

}
