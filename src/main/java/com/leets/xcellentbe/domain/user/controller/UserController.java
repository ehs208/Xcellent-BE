package com.leets.xcellentbe.domain.user.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.dto.UserLoginRequestDto;
import com.leets.xcellentbe.domain.user.dto.UserProfileRequestDto;
import com.leets.xcellentbe.domain.user.dto.UserProfileResponseDto;
import com.leets.xcellentbe.domain.user.dto.UserSignUpRequestDto;
import com.leets.xcellentbe.domain.user.service.S3UploadService;
import com.leets.xcellentbe.domain.user.service.UserService;
import com.leets.xcellentbe.global.auth.email.EmailRequestDto;
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

	@PutMapping("/info")
	@Operation(summary = "프로필 수정", description = "사용자의 프로필을 수정합니다.")
	public ResponseEntity<GlobalResponseDto<String>> updateProfile(@RequestBody UserProfileRequestDto userProfileRequestDto, HttpServletRequest request) {
		userService.updateProfile(request,userProfileRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}

	@PatchMapping("/profile-image")
	@Operation(summary = "프로필 이미지 수정", description = "사용자의 프로필 이미지를 수정합니다.")
	public ResponseEntity<GlobalResponseDto<String>> updateProfileImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
			return ResponseEntity.status(HttpStatus.OK)
				.body(GlobalResponseDto.success(userService.updateProfileImage(file, request)));

	}

	@PatchMapping("/background-image")
	@Operation(summary = "배경 이미지 수정", description = "사용자의 배경 이미지를 수정합니다.")
	public ResponseEntity<GlobalResponseDto<String>> updateBackgroundImage (@RequestParam("file") MultipartFile file, HttpServletRequest request){
			return ResponseEntity.status(HttpStatus.OK)
				.body(GlobalResponseDto.success(userService.updateBackgroundProfileImage(file, request)));
		}
	}
