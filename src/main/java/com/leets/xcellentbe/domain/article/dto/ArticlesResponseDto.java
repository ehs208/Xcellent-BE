package com.leets.xcellentbe.domain.article.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
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
	private List<String> hashtags;
	// private Article rePost;
	private List<String> filePath;

	@Builder
	private ArticlesResponseDto(String writer, String content, Boolean isPinned, List<String> hashtags
		, List<String> filePath) {
		this.writer = writer;
		this.content = content;
		this.isPinned = isPinned;
		this.hashtags = hashtags;
		// this.rePost = rePost;
		this.filePath = filePath;
	}

	public static ArticlesResponseDto of(Article article) {
		return ArticlesResponseDto.builder()
			.writer(article.getWriter().getCustomId())
			.content(article.getContent())
			.hashtags(article.getHashtags().stream()
				.map(Hashtag::getContent)
				.collect(Collectors.toList()))
			// .rePost(article.getRePost())
			.filePath(article.getMediaList().stream()
				.map(ArticleMedia::getFilePath)
				.collect(Collectors.toList()))
			.build();
	}
}
