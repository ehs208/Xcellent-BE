package com.leets.xcellentbe.domain.comment.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.comment.dto.CommentCreateRequestDto;
import com.leets.xcellentbe.domain.comment.dto.CommentResponseDto;
import com.leets.xcellentbe.domain.comment.service.CommentService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	//댓글 작성
	@PostMapping()
	@Operation(summary = "댓글 작성", description = "새 댓글을 작성합니다.")
	public ResponseEntity<GlobalResponseDto<Void>> createComment(
		HttpServletRequest request,
		@RequestBody CommentCreateRequestDto commentCreateRequestDto,
		@PathVariable UUID articleId,
		@RequestParam(value = "parentCommentId", required = false) UUID parentCommentId){
		commentService.createComment(request, commentCreateRequestDto, articleId, parentCommentId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}

	//댓글 삭제(소프트)
	@PatchMapping("/{commentId}")
	@Operation(summary = "댓글 삭제", description = "댓글 삭제(상태 변경)합니다.")
	public ResponseEntity<GlobalResponseDto<Void>> deleteComment(
		HttpServletRequest request,
		@PathVariable UUID commentId){
		commentService.deleteComment(request, commentId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}

	//대댓글 삭제(소프트)
	@PatchMapping("/{commentId}/{replyId}")
	@Operation(summary = "대댓글 삭제", description = "대댓글을 소프트 삭제합니다.")
	public ResponseEntity<GlobalResponseDto<Void>> deleteReply(
		HttpServletRequest request,
		@PathVariable UUID commentId,
		@PathVariable UUID replyId) {
		commentService.deleteReply(request, commentId, replyId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}

	//댓글 조회
	@GetMapping("/{commentId}")
	@Operation(summary = "댓글 조회", description = "해당 ID의 댓글을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<CommentResponseDto>> getComment(
		HttpServletRequest request,
		@PathVariable UUID commentId){
		CommentResponseDto commentResponseDto = commentService.getComment(request, commentId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(commentResponseDto));
	}
}
