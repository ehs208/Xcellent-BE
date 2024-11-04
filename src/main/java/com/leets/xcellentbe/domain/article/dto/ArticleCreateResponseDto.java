package com.leets.xcellentbe.domain.article.dto;

import java.util.UUID;

import com.leets.xcellentbe.domain.article.domain.Article;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleCreateResponseDto {

	private UUID articleId;
	private String message;

	@Builder
	private ArticleCreateResponseDto(UUID articleId, String message) {
		this.articleId = articleId;
		this.message = message;
	}

	public static ArticleCreateResponseDto from(Article article) {
		return ArticleCreateResponseDto.builder()
			.articleId(article.getArticleId())
			.message("게시글이 작성되었습니다.")
			.build();
	}
}
