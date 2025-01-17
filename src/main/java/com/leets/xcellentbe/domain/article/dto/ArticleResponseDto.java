package com.leets.xcellentbe.domain.article.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;
import com.leets.xcellentbe.domain.comment.dto.CommentResponseDto;
import com.leets.xcellentbe.domain.comment.dto.CommentStatsDto;
import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleResponseDto {
	private UUID articleId;
	private String customId;
	private String userName;
	private String content;
	private DeletedStatus deletedStatus;
	private List<String> hashtags;
	private UUID rePostId;
	private List<String> mediaUrls;
	private List<CommentResponseDto> comments;
	private int viewCnt;
	private long rePostCnt;
	private long likeCnt;
	private long commentCnt;
	private boolean owner;
	private boolean isLiked;
	private LocalDateTime createdAt;

	@Builder
	private ArticleResponseDto(UUID articleId, String userName, String customId, String content,
		DeletedStatus deletedStatus,
		List<String> hashtags, UUID rePostId, List<String> mediaUrls, List<CommentResponseDto> comments,
		int viewCnt, long rePostCnt, long likeCnt, long commentCnt, boolean owner, boolean isLiked,
		LocalDateTime createdAt) {
		this.articleId = articleId;
		this.userName = userName;
		this.customId = customId;
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
		this.isLiked = isLiked;
		this.createdAt = createdAt;
	}

	public static ArticleResponseDto from(Article article, boolean isOwner, boolean isLiked, ArticleStatsDto stats,
		Map<UUID, CommentStatsDto> replyStatsMap) {
		return ArticleResponseDto.builder()
			.articleId(article.getArticleId())
			.content(article.getContent())
			.deletedStatus(article.getDeletedStatus())
			.userName(article.getWriter().getUserName())
			.customId(article.getWriter().getCustomId())
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
				.filter(comment -> comment != null
					&& comment.getDeletedStatus() == DeletedStatus.NOT_DELETED) // null 및 삭제된 댓글 필터링
				.map(comment -> {
					CommentStatsDto commentStats = replyStatsMap.getOrDefault(comment.getCommentId(),
						CommentStatsDto.from(0, 0));
					boolean isCommentOwner = comment.getWriter().getUserId().equals(article.getWriter().getUserId());
					return CommentResponseDto.from(comment, isCommentOwner, commentStats, replyStatsMap, 1); // 깊이 1로 제한
				})
				.collect(Collectors.toList()) : null)
			.viewCnt(article.getViewCnt())
			.rePostCnt(stats.getRepostCnt())
			.likeCnt(stats.getLikeCnt())
			.commentCnt(stats.getCommentCnt())
			.owner(isOwner)
			.isLiked(isLiked)
			.createdAt(article.getCreatedAt())
			.build();
	}
}
