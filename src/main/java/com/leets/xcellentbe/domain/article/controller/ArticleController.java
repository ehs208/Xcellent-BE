package com.leets.xcellentbe.domain.article.controller;

import java.time.LocalDateTime;
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
import com.leets.xcellentbe.domain.article.dto.ArticleRepostDto;
import com.leets.xcellentbe.domain.article.dto.ArticleRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleResponseDto;
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

	//게시글 작성
	@PostMapping("/create")
	@Operation(summary = "게시글 작성", description = "새 게시글을 작성합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleCreateResponseDto>> createArticle(
		HttpServletRequest request,
		@RequestBody ArticleCreateRequestDto articleCreateRequestDto,
		@RequestParam("mediaFiles") List<MultipartFile> mediaFiles){
		ArticleCreateResponseDto responseDto = articleService.createArticle(request, articleCreateRequestDto, mediaFiles);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(responseDto));
	}

	//게시글 삭제(소프트)
	@PatchMapping("/{articleId}/delete")
	@Operation(summary = "게시글 삭제", description = "게시글을 소프트 삭제(상태 변경)합니다.")
	public ResponseEntity<GlobalResponseDto<Void>> deleteArticle(
		HttpServletRequest request,
		@PathVariable UUID articleId){
		articleService.deleteArticle(request, articleId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}

	//게시글 조회
	@GetMapping("/{articleId}")
	@Operation(summary = "게시글 조회", description = "해당 ID의 게시글을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleResponseDto>> getArticle(
		@RequestBody ArticleRequestDto articleRequestDto){
		ArticleResponseDto articleResponseDto = articleService.getArticle(articleRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(articleResponseDto));
	}

	//메인 페이지 게시글 조회
	@GetMapping
	@Operation(summary = "게시글 목록 조회(스크롤)", description = "커서 페이징을 적용하여 게시글 목록을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<List<ArticleResponseDto>>> getArticles(
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
		@RequestParam(defaultValue = "10") int size) {
		List<ArticleResponseDto> articles = articleService.getArticles(cursor, size);
		return ResponseEntity.ok(GlobalResponseDto.success(articles));
	}

	//리포스트 작성
	@PostMapping("/{articleId}/repost")
	@Operation(summary = "게시글 리포스트", description = "게시글을 리포스트합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleCreateResponseDto>> rePostArticle(
		HttpServletRequest request,
		@RequestBody ArticleRepostDto articleRepostDto){
		ArticleCreateResponseDto responseDto = articleService.rePostArticle(request, articleRepostDto);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(responseDto));
	}

	//리포스트 삭제
	@PatchMapping("/{articleId}/deleteRepost")
	@Operation(summary = "리포스트 삭제", description = "리포스트를 삭제합니다.")
	public ResponseEntity<GlobalResponseDto<Void>> deleteRepost(
		HttpServletRequest request,
		@PathVariable UUID articleId){
		articleService.deleteRepost(request, articleId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}
}
