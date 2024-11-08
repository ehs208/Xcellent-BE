package com.leets.xcellentbe.domain.article.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.article.dto.ArticleCreateRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleCreateResponseDto;
import com.leets.xcellentbe.domain.article.dto.ArticleResponseDto;
import com.leets.xcellentbe.domain.article.dto.ArticlesResponseDto;
import com.leets.xcellentbe.domain.article.service.ArticleService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article")
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

	//게시글 작성
	@PostMapping
	@Operation(summary = "게시글 작성", description = "새 게시글을 작성합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleCreateResponseDto>> createArticle(
		HttpServletRequest request,
		@RequestBody ArticleCreateRequestDto articleCreateRequestDto,
		@RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles) {
		if (mediaFiles == null) {
			mediaFiles = Collections.emptyList();
		}
		ArticleCreateResponseDto responseDto = articleService.createArticle(request, articleCreateRequestDto,
			mediaFiles);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(responseDto));
	}

	//게시글 삭제(소프트)
	@PatchMapping("/{articleId}")
	@Operation(summary = "게시글 삭제", description = "게시글을 삭제(상태 변경)합니다.")
	public ResponseEntity<GlobalResponseDto<Void>> deleteArticle(
		HttpServletRequest request,
		@PathVariable UUID articleId) {
		articleService.deleteArticle(request, articleId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}

	//게시글 조회
	@GetMapping("/{articleId}")
	@Operation(summary = "게시글 조회", description = "해당 ID의 게시글을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleResponseDto>> getArticle(
		HttpServletRequest request,
		@PathVariable UUID articleId) {
		ArticleResponseDto articleResponseDto = articleService.getArticle(request, articleId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(articleResponseDto));
	}

	//메인 페이지 게시글 조회
	@GetMapping
	@Operation(summary = "게시글 목록 조회(스크롤)", description = "페이징을 적용하여 게시글 목록을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<List<ArticleResponseDto>>> getArticles(
		HttpServletRequest request,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
		@RequestParam(defaultValue = "10") int size) {
		List<ArticleResponseDto> articles = articleService.getArticles(request, cursor, size);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(articles));
	}

	//리포스트 작성
	@PostMapping("/{articleId}/repost")
	@Operation(summary = "게시글 리포스트", description = "게시글을 리포스트합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleCreateResponseDto>> rePostArticle(
		HttpServletRequest request,
		@PathVariable UUID articleId) {
		ArticleCreateResponseDto responseDto = articleService.rePostArticle(request, articleId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(responseDto));
	}

	//리포스트 삭제
	@PatchMapping("/{articleId}/unRepost")
	@Operation(summary = "리포스트 삭제", description = "리포스트를 삭제합니다.")
	public ResponseEntity<GlobalResponseDto<Void>> deleteRepost(
		HttpServletRequest request,
		@PathVariable UUID articleId) {
		articleService.deleteRepost(request, articleId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());

	}
}
