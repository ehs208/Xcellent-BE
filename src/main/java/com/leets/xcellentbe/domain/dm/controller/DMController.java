package com.leets.xcellentbe.domain.dm.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.chatRoom.service.ChatRoomService;
import com.leets.xcellentbe.domain.dm.dto.DMDto;
import com.leets.xcellentbe.domain.dm.service.DMService;
import com.leets.xcellentbe.global.redis.RedisPublisher;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DMController {

	private final RedisPublisher redisPublisher;
	private final ChatRoomService chatRoomService;
	private final DMService dmService;

	@MessageMapping("/dm")
	@Operation(summary = "채팅을 시작", description = "채팅을 시작합니다.")
	public void message(DMDto dmDto) {
		chatRoomService.enterChatRoom(dmDto.receiverID());
		redisPublisher.publish(chatRoomService.getTopic(dmDto.receiverID()), dmDto);

		dmService.saveMessage(dmDto);
	}

	@PostMapping("/dm")
	@Operation(summary = "채팅을 시작", description = "채팅을 시작합니다.")
	public void startChat(
		@Parameter(description = "메시지 전송 객체") @RequestBody DMDto dmDto) {
		chatRoomService.enterChatRoom(dmDto.receiverID());
		redisPublisher.publish(chatRoomService.getTopic(dmDto.receiverID()), dmDto);

		dmService.saveMessage(dmDto);

	}

	@GetMapping("/api/chat-room/{chatRoomId}/dm")
	@Operation(summary = "채팅방 로드", description = "채팅방을 로드합니다.")
	public ResponseEntity<GlobalResponseDto<List<DMDto>>> loadMessage(@PathVariable UUID chatRoomId) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(GlobalResponseDto.success(dmService.loadMessage(chatRoomId)));
	}

	@PatchMapping("/api/chat-room/delete/{dmId}")
	@Operation(summary = "메시지 삭제", description = "메시지를 삭제합니다.")
	public ResponseEntity<GlobalResponseDto<String>> deleteDM(@PathVariable UUID dmId) {
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(dmService.deleteDM(dmId)));
	}
}
