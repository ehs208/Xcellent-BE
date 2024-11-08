package com.leets.xcellentbe.domain.article.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.xcellentbe.domain.article.dto.ArticlesResponseDto;
import com.leets.xcellentbe.domain.article.service.ArticleService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;

	@GetMapping("/{customId}/list")
	@Operation(summary = "특정 사용자의 게시글 조회", description = "특정 사용자의 게시글을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<List<ArticlesResponseDto>>> getArticles(@PathVariable String customId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(articleService.getArticles(customId, false)));
	}

	@GetMapping("/{customId}/list/media")
	@Operation(summary = "특정 사용자의 미디어 게시글 조회", description = "특정 사용자의 미디어 게시글을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<List<ArticlesResponseDto>>> getMediaArticles(
		@PathVariable String customId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(articleService.getArticles(customId, true)));
	}
}
