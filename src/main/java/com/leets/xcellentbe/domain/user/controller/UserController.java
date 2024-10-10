package com.leets.xcellentbe.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.user.dto.UserLoginRequestDto;
import com.leets.xcellentbe.domain.user.dto.UserSignUpRequestDto;
import com.leets.xcellentbe.domain.user.service.UserService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("/auth/register")
	@Operation(summary = "회원가입", description = "회원가입을 합니다.")
	public ResponseEntity<GlobalResponseDto<String>> register(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(GlobalResponseDto.success(userService.register(userSignUpRequestDto), HttpStatus.CREATED.value()));
	}

	@Operation(summary = "로그인", description = "사용자의 이메일과 비밀번호로 로그인합니다.")
	@PostMapping("/auth/login")
	public String login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
		// 로그인 로직 처리
		return "로그인 성공";
	}
}
