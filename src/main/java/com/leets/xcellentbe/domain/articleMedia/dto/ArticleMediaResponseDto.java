package com.leets.xcellentbe.domain.articleMedia.dto;

import java.util.UUID;

import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleMediaResponseDto {
	private UUID articleMedia;
	private String filePath;

	@Builder
	private ArticleMediaResponseDto(UUID articleMedia, String filePath) {
		this.articleMedia = articleMedia;
		this.filePath = filePath;
	}

	public static ArticleMediaResponseDto from(ArticleMedia articleMedia) {
		return ArticleMediaResponseDto.builder()
			.articleMedia(articleMedia.getArticleMediaId())
			.filePath(articleMedia.getFilePath())
			.build();
	}
}
