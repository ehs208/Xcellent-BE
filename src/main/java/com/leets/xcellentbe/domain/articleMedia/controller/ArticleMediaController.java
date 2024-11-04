package com.leets.xcellentbe.domain.articleMedia.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.articleMedia.dto.ArticleMediaRequestDto;
import com.leets.xcellentbe.domain.articleMedia.dto.ArticleMediaResponseDto;
import com.leets.xcellentbe.domain.articleMedia.exception.ArticleMediaNotFoundException;
import com.leets.xcellentbe.domain.articleMedia.service.ArticleMediaService;
import com.leets.xcellentbe.global.response.GlobalResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/articleMedia")
@RequiredArgsConstructor
public class ArticleMediaController {
	private final ArticleMediaService articleMediaService;

	//미디어 생성
	@PostMapping("/{articleId}/upload")
	@Operation(summary = "게시글 미디어 업로드", description = "게시글의 미디어 파일을 업로드합니다.")
	public ResponseEntity<GlobalResponseDto<List<ArticleMediaResponseDto>>> saveArticleMedia(
		@PathVariable UUID articleId,
		@RequestParam("mediaFiles") List<MultipartFile> mediaFiles,
		@RequestBody ArticleMediaRequestDto articleMediaRequestDto) {

		List<ArticleMediaResponseDto> responseDtos = articleMediaService.saveArticleMedia(mediaFiles, articleMediaRequestDto);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success(responseDtos));
	}
	//미디어 단건 조회
	@GetMapping("/{mediaId}")
	@Operation(summary = "미디어 단건 조회", description = "특정 미디어 파일의 정보를 조회합니다.")
	public ResponseEntity<GlobalResponseDto<ArticleMediaResponseDto>> getMedia(
		@PathVariable UUID mediaId,
		@RequestBody ArticleMediaRequestDto articleMediaRequestDto) {
		articleMediaService.getArticleMedia(articleMediaRequestDto);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success());
	}
	//미디어 단건 삭제
	@DeleteMapping("/{mediaId}")
	@Operation(summary = "미디어 단건 삭제", description = "특정 미디어 파일을 삭제합니다.")
	public ResponseEntity<GlobalResponseDto<String>> deleteArticleMedia(
		@PathVariable UUID mediaId,
		@RequestBody ArticleMediaRequestDto articleMediaRequestDto) {
		articleMediaService.deleteArticleMedia(articleMediaRequestDto);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success());
	}
	//미디어 전체 삭제
	@DeleteMapping("/{articleId}/delete-all")
	@Operation(summary = "게시글의 모든 미디어 삭제", description = "특정 게시글의 모든 미디어 파일을 삭제합니다.")
	public ResponseEntity<GlobalResponseDto<String>> deleteAllArticleMedia(
		@PathVariable UUID articleId,
		@RequestBody ArticleMediaRequestDto articleMediaRequestDto) {
		articleMediaService.deleteAllMediaByArticle(articleMediaRequestDto);
		return ResponseEntity.status(HttpStatus.OK)
			.body(GlobalResponseDto.success());
	}
}
