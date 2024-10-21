package com.leets.xcellentbe.global.auth.login.oauth;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		Map<String, Object> attributes = oAuth2User.getAttributes();

		OAuthAttributes extractAttributes = OAuthAttributes.of(userNameAttributeName, attributes);

		User createdUser = getUser(extractAttributes);

		return new CustomOAuthUser(
			Collections.singleton(new SimpleGrantedAuthority(createdUser.getUserRole().getKey())),
			attributes,
			extractAttributes.getNameAttributeKey(),
			createdUser.getEmail(),
			createdUser.getUserRole()
		);
	}

	private User getUser(OAuthAttributes attributes) {
		return userRepository.findByEmail(attributes.getGoogleOAuthUserInfo().getEmail())
			.orElseGet(() -> saveUser(attributes));
	}

	private User saveUser(OAuthAttributes attributes) {
		User createdUser = attributes.toEntity(attributes.getGoogleOAuthUserInfo());
		return userRepository.save(createdUser);
	}
}
