package com.leets.xcellentbe.domain.articleLike.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.articleLike.dto.ArticleLikeResponseDto;
import com.leets.xcellentbe.domain.articleLike.service.ArticleLikeService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article/{articleId}")
@RequiredArgsConstructor
public class ArticleLikeController {

	private final ArticleLikeService articleLikeService;

	@PostMapping("/like")
	@Operation(summary = "신규 좋아요 등록", description = "게시글에 좋아요 했습니다.")
	public ResponseEntity<GlobalResponseDto<ArticleLikeResponseDto>> articleLike(
		HttpServletRequest request,
		@PathVariable UUID articleId){
		ArticleLikeResponseDto responseDto = articleLikeService.likeArticle(request, articleId);

		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(responseDto));
	}

	@PatchMapping("/unlike")
	@Operation(summary = "좋아요 삭제", description = "게시글에 좋아요를 취소했습니다.")
	public ResponseEntity<GlobalResponseDto<String>> articleUnLike(
		HttpServletRequest request,
		@PathVariable UUID articleId){
		articleLikeService.unLike(request, articleId);

		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success());
	}
}
