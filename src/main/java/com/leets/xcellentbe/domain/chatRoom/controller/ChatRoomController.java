package com.leets.xcellentbe.domain.chatRoom.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.chatRoom.dto.ChatRoomDto;
import com.leets.xcellentbe.domain.chatRoom.service.ChatRoomService;
import com.leets.xcellentbe.domain.dm.dto.request.DMRequest;
import com.leets.xcellentbe.domain.dm.dto.response.DMResponse;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

	@PostMapping()
	@Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다.")
	public ResponseEntity<GlobalResponseDto<DMResponse>> createChatRoom(@RequestBody DMRequest dmRequest,
		@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(GlobalResponseDto.success(chatRoomService.createChatRoom(dmRequest, userDetails)));
	}

	@GetMapping("/all")
	@Operation(summary = "사용자 채팅방 전체 조회", description = "사용자의 모든 채팅방을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<List<DMResponse>>> findAllChatRoomByUser(
		@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(chatRoomService.findAllChatRoomByUser(userDetails)));
	}

	@GetMapping("/{chatRoomId}")
	@Operation(summary = "사용자 채팅방 조회", description = "사용자의 채팅방을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<ChatRoomDto>> findChatRoom(@PathVariable UUID chatRoomId,
		@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(chatRoomService.findChatRoom(chatRoomId, userDetails)));
	}

	@PatchMapping("/{chatRoomId}")
	@Operation(summary = "채팅방 삭제", description = "채팅방을 삭제합니다.")
	public ResponseEntity<GlobalResponseDto<String>> deleteChatRoom(@PathVariable UUID chatRoomId,
		@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(chatRoomService.deleteChatRoom(chatRoomId, userDetails)));
	}
}
