package com.leets.xcellentbe.global.auth.login.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Optional;

import com.leets.xcellentbe.domain.user.Role;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.global.auth.jwt.JwtService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        CustomOAuthUser oAuth2User = (CustomOAuthUser) authentication.getPrincipal();
        Optional<User> user = userRepository.findByEmail(oAuth2User.getEmail());
        // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
        if (oAuth2User.getRole() == Role.GUEST) {
            String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
            response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
            response.sendRedirect("/oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 ��다이렉트
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
