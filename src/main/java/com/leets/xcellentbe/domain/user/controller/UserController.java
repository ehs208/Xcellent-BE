package com.leets.xcellentbe.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.user.dto.UserLoginDto;
import com.leets.xcellentbe.domain.user.dto.UserSignUpDto;
import com.leets.xcellentbe.domain.user.service.UserService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userservice;

	@PostMapping("/auth/register")
	@Operation(summary = "회원가입", description = "회원가입을 합니다.")
	public ResponseEntity register(@RequestBody UserSignUpDto userSignUpDto) {
		return ResponseEntity.ok(GlobalResponseDto.success(userservice.register(userSignUpDto)));
	}

	@Operation(summary = "로그인", description = "사용자의 이메일과 비밀번호로 로그인합니다.")
	@PostMapping("/login")
	public String login(@RequestBody UserLoginDto userLoginDto) {
		// 로그인 로직 처리
		return "로그인 성공";
	}
}
