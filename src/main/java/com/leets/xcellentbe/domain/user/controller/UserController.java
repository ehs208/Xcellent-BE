package com.leets.xcellentbe.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.user.dto.UserSignUpDto;
import com.leets.xcellentbe.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userservice;

	@PostMapping("/register")
	@Operation(summary = "회원가입", description = "회원가입을 합니다.")
	public ResponseEntity<Void> register(@RequestBody UserSignUpDto userSignUpDto) {
		return ResponseEntity.ok(userservice.register(userSignUpDto));
	}

}
