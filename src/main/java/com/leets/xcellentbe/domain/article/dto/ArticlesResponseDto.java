package com.leets.xcellentbe.domain.article.dto;

import java.util.List;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ArticlesResponseDto {
	private String writer;
	private String content;
	private Boolean isPinned;
	private List<Hashtag> hashtags;
	private Article rePost;
	private List<String> filePath;

	@Builder
	private ArticlesResponseDto(String writer, String content, Boolean isPinned, List<Hashtag> hashtags,
		Article rePost,
		List<String> filePath) {
		this.writer = writer;
		this.content = content;
		this.isPinned = isPinned;
		this.hashtags = hashtags;
		this.rePost = rePost;
		this.filePath = filePath;
	}

	public static ArticlesResponseDto of(Article article, List<String> filePath) {
		return ArticlesResponseDto.builder()
			.writer(article.getWriter().getUserName())
			.content(article.getContent())
			.isPinned(article.getIsPinned())
			.hashtags(article.getHashtags())
			.rePost(article.getReArticle())
			.filePath(filePath)
			.build();
	}
}
