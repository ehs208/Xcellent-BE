package com.leets.xcellentbe.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.user.dto.UserLoginRequestDto;
import com.leets.xcellentbe.domain.user.dto.UserProfileResponseDto;
import com.leets.xcellentbe.domain.user.dto.UserSignUpRequestDto;
import com.leets.xcellentbe.domain.user.service.UserService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/info")
	@Operation(summary = "프로필 조회", description = "사용자의 프로필 내용을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<UserProfileResponseDto>> getProfile(HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(userService.getProfile(request)));
	}

	@PatchMapping("/{userId}")
	@Operation(summary = "프로필 수정", description = "사용자의 프로필을 수정합니다.")
	public String updateProfile(@RequestBody UserLoginRequestDto userLoginRequestDto) {
		return "로그인 성공";
	}
}
