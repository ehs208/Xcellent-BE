package com.leets.xcellentbe.domain.article.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.comment.dto.CommentResponseDto;
import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleResponseDto {
	private UUID articleId;
	private Long writerId;
	private String content;
	private DeletedStatus deletedStatus;
	private List<String> hashtags;
	private UUID rePostId;
	private List<String> mediaUrls;
	private List<CommentResponseDto> comments;
	private int viewCnt;
	private int rePostCnt;
	private int likeCnt;
	private int commentCnt;
	private boolean owner;

	@Builder
	private ArticleResponseDto(UUID articleId, Long writerId, String content, DeletedStatus deletedStatus,
								List<String> hashtags, UUID rePostId, List<String> mediaUrls, List<CommentResponseDto> comments,
								int viewCnt, int rePostCnt, int likeCnt, int commentCnt, boolean owner) {
		this.articleId = articleId;
		this.writerId = writerId;
		this.content = content;
		this.deletedStatus = deletedStatus;
		this.hashtags = hashtags;
		this.rePostId = rePostId;
		this.mediaUrls = mediaUrls;
		this.viewCnt = viewCnt;
		this.rePostCnt = rePostCnt;
		this.likeCnt = likeCnt;
		this.commentCnt = commentCnt;
		this.owner = owner;
		this.comments = comments;
	}

	public static ArticleResponseDto from(Article article, boolean isOwner) {
		return ArticleResponseDto.builder()
			.articleId(article.getArticleId())
			.content(article.getContent())
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
			.comments(article.getComments() != null ? article.getComments()
				.stream()
				.map(comment -> CommentResponseDto.from(comment,comment.getWriter().getUserId().equals(article.getWriter().getUserId())))
				.collect(Collectors.toList()) : null)
			.viewCnt(article.getViewCnt())
			.rePostCnt(article.getRepostCnt())
			.likeCnt(article.getLikeCnt())
			.commentCnt(article.getCommentCnt())
			.owner(isOwner)
			.build();
	}
}
