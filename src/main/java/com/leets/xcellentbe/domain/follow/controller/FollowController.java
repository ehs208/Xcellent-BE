package com.leets.xcellentbe.domain.follow.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.follow.dto.FollowRequestDto;
import com.leets.xcellentbe.domain.follow.dto.FollowInfoResponseDto;
import com.leets.xcellentbe.domain.follow.service.FollowService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class FollowController {
	private final FollowService followService;

	@PostMapping("/follow")
	@Operation(summary = "팔로우", description = "다른 사용자를 팔로우합니다.")
	public ResponseEntity<GlobalResponseDto<FollowRequestDto>> followUser(@RequestBody FollowRequestDto requestDto,
		HttpServletRequest request) {
		followService.followUser(requestDto, request);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success());
	}

	@DeleteMapping("/follow")
	@Operation(summary = "언팔로우", description = "다른 사용자를 언팔로우합니다.")
	public ResponseEntity<GlobalResponseDto<FollowRequestDto>> unfollowerUser(@RequestBody FollowRequestDto requestDto,
		HttpServletRequest request) {
		followService.unfollowUser(requestDto, request);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success());
	}

	@GetMapping("/following")
	@Operation(summary = "팔로잉 조회", description = "사용자가 팔로우하는 사용자 목록을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<Page<FollowInfoResponseDto>>> getFollowingList(
		@RequestParam(required = false, defaultValue = "1") int pageNo, @RequestParam String customId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(followService.getFollowingList(customId, pageNo - 1)));
	}

	@GetMapping("/follower")
	@Operation(summary = "팔로워 조회", description = "사용자를 팔로우하는 사용자 목록을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<Page<FollowInfoResponseDto>>> getFollowerList(
		@RequestParam(required = false, defaultValue = "1") int pageNo, @RequestParam String customId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(followService.getFollowerList(customId, pageNo - 1)));
	}
}
