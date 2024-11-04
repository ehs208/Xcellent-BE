package com.leets.xcellentbe.domain.article.controller;

import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.article.dto.ArticleCreateRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleDeleteRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleRequestDto;
import com.leets.xcellentbe.domain.article.dto.ArticleResponseDto;
import com.leets.xcellentbe.domain.article.service.ArticleService;
import com.leets.xcellentbe.domain.articleMedia.dto.ArticleMediaRequestDto;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
	private final ArticleService articleService;
	//게시글 작성
	@PostMapping("/create")
	@Operation(summary = "게시글 작성", description = "새 게시글을 작성합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleResponseDto>> createArticle(
		HttpServletRequest request,
		@RequestBody ArticleCreateRequestDto articleCreateRequestDto,
		@RequestParam("mediaFiles") List<MultipartFile> mediaFiles){
		ArticleResponseDto responseDto = articleService.createArticle(request, articleCreateRequestDto, mediaFiles);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(responseDto));
	}
	//게시글 고정
	@PatchMapping("/{articleId}/pin")
	@Operation(summary = "게시글 고정", description = "해당 게시글을 계정당 하나만 고정 상태로 설정합니다.")
	public ResponseEntity<GlobalResponseDto<String>> pinArticle(
		@PathVariable UUID articleId,
		@RequestParam Long accountId){
		articleService.pinArticle(articleId, accountId);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}
	//게시글 수정
	@PatchMapping("/{articleId}/update")
	@Operation(summary = "게시글 수정", description = "기존 게시글의 내용을 수정합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleResponseDto>> updateArticle(
		@PathVariable UUID articleId,
		@RequestBody ArticleRequestDto articleRequestDto){
		articleService.updateArticle(articleRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}
	//게시글 삭제(소프트)
	@PatchMapping("/{articleId}/delete")
	@Operation(summary = "게시글 삭제", description = "게시글을 소프트 삭제(상태 변경)합니다.")
	public ResponseEntity<GlobalResponseDto<Void>> deleteArticle(
		@PathVariable UUID articleId,
		@RequestBody ArticleDeleteRequestDto articleDeleteRequestDto,
		@RequestBody ArticleMediaRequestDto articleMediaRequestDto){
		articleService.deleteArticle(articleDeleteRequestDto, articleMediaRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success());
	}
	//게시글 조회
	@GetMapping("/{articleId")
	@Operation(summary = "게시글 조회", description = "해당 ID의 게시글을 조회합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleResponseDto>> getArticle(
		@PathVariable UUID articleId,
		@RequestBody ArticleRequestDto articleRequestDto){
		ArticleResponseDto articleResponseDto = articleService.getArticle(articleRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(GlobalResponseDto.success(articleResponseDto));
	}
}
