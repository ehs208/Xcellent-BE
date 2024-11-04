package com.leets.xcellentbe.domain.article.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleResponseDto {
	private UUID articleId;
	private String content;
	private Boolean isPinned;
	private DeletedStatus deletedStatus;
	private Long writerId;
	private List<String> hashtags;
	private UUID rePostId;
	private List<String> mediaUrls;

	@Builder
	private ArticleResponseDto(UUID articleId, String content, Boolean isPinned, DeletedStatus deletedStatus,
		Long writerId, List<String> hashtags, UUID rePostId, List<String> mediaUrls) {
		this.articleId = articleId;
		this.content = content;
		this.isPinned = isPinned;
		this.deletedStatus = deletedStatus;
		this.writerId = writerId;
		this.hashtags = hashtags;
		this.rePostId = rePostId;
		this.mediaUrls = mediaUrls;
	}

	public static ArticleResponseDto from(Article article) {
		return ArticleResponseDto.builder()
			.articleId(article.getArticleId())
			.content(article.getContent())
			.isPinned(article.getIsPinned())
			.deletedStatus(article.getDeletedStatus())
			.writerId(article.getWriter().getUserId())
			.hashtags(article.getHashtags() != null ? article.getHashtags()
				.stream()
				.map(Hashtag::getContent)
				.collect(Collectors.toList()) : null)
			.rePostId(article.getRePost() != null ? article.getRePost().getArticleId() : null)
			.mediaUrls(article.getMediaList() != null ? article.getMediaList()
				.stream()
				.map(ArticleMedia::getFilePath) // 이미지 URL로 매핑
				.collect(Collectors.toList()) : null)
			.build();
	}
}
