package com.leets.xcellentbe.global.auth.login.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leets.xcellentbe.domain.user.Role;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.global.auth.jwt.JwtService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtService jwtService;
	private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		log.info("OAuth2 Login 성공!");

		Map<String, String> tokenMap = new HashMap<>();

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		CustomOAuthUser oAuth2User = (CustomOAuthUser)authentication.getPrincipal();
		Optional<User> user = userRepository.findByEmail(oAuth2User.getEmail());
		// User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
		if (oAuth2User.getRole() == Role.GUEST) {
			String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());

			tokenMap.put("accessToken", accessToken);
			GlobalResponseDto<Map<String, String>> responseDto = GlobalResponseDto.success(tokenMap);

			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);

			String json = mapper.writeValueAsString(responseDto);

			response.getWriter().write(json);
			// response.sendRedirect("/oauth2/sign-up.html");

			jwtService.sendAccessAndRefreshToken(response, accessToken, null);
		} else {
			loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
		}
	}

	private void loginSuccess(HttpServletResponse response, CustomOAuthUser oAuth2User) throws IOException {
		String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
		String refreshToken = jwtService.createRefreshToken();
		response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
		response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

		jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
	}
}
