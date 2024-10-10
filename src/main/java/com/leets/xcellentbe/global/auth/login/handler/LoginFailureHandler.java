package com.leets.xcellentbe.global.auth.login.handler;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

/**
 * JWT 로그인 실패 시 처리하는 핸들러
 * SimpleUrlAuthenticationFailureHandler를 상속받아서 구현
 */
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		ErrorCode errorCode = exception instanceof BadCredentialsException ? ErrorCode.LOGIN_FAIL : ErrorCode.USER_NOT_FOUND;
		GlobalResponseDto<String> responseDto = GlobalResponseDto.fail(ErrorCode.valueOf(errorCode.getCode()),  errorCode.getMessage());

		response.setStatus(errorCode.getStatus());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String json = mapper.writeValueAsString(responseDto);
		response.getWriter().write(json);
		log.info("로그인에 실패했습니다. 메시지 : {}", errorCode.getMessage());
	}
}
