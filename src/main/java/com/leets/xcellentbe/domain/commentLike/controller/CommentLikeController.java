package com.leets.xcellentbe.domain.commentLike.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.leets.xcellentbe.domain.commentLike.dto.CommentLikeResponseDto;
import com.leets.xcellentbe.domain.commentLike.service.CommentLikeService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article/{articleId}/{commentId}")
@RequiredArgsConstructor
public class CommentLikeController {

	private final CommentLikeService commentLikeService;

	@PostMapping("/like")
	@Operation(summary = "신규 댓글 좋아요 등록", description = "댓글에 좋아요 했습니다.")
	public ResponseEntity<GlobalResponseDto<CommentLikeResponseDto>> commentLike(
		HttpServletRequest request,
		@PathVariable UUID commentId){
		CommentLikeResponseDto responseDto = commentLikeService.likeComment(request, commentId);

		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(responseDto));
	}

	@PatchMapping("/unlike")
	@Operation(summary = "댓글 좋아요 삭제", description = "댓글에 좋아요를 취소했습니다.")
	public ResponseEntity<GlobalResponseDto<String>> commentUnLike(
		HttpServletRequest request,
		@PathVariable UUID commentId){
		commentLikeService.unLikeComment(request, commentId);

		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success());
	}
}
