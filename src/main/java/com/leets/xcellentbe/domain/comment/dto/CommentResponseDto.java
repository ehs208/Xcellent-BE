package com.leets.xcellentbe.domain.comment.dto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.leets.xcellentbe.domain.comment.domain.Comment;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
	private UUID commentId;
	private String customId;
	private String userName;
	private String content;
	private DeletedStatus deletedStatus;
	private UUID rePostId;
	private int viewCnt;
	private long likeCnt;
	private long commentCnt;
	private boolean owner;
	private List<CommentResponseDto> comments;

	@Builder
	private CommentResponseDto(UUID commentId, String userName, String customId, String content, DeletedStatus deletedStatus,
		UUID rePostId, int viewCnt, long likeCnt, long commentCnt, boolean owner, List<CommentResponseDto> comments) {
		this.commentId = commentId;
		this.userName = userName;
		this.customId = customId;
		this.content = content;
		this.deletedStatus = deletedStatus;
		this.rePostId = rePostId;
		this.viewCnt = viewCnt;
		this.likeCnt = likeCnt;
		this.commentCnt = commentCnt;
		this.owner = owner;
		this.comments = comments;
	}

	public static CommentResponseDto from(Comment comment, boolean isOwner, CommentStatsDto stats, Map<UUID, CommentStatsDto> replyStatsMap, int depth) {
		if (depth <= 0 || comment == null) { // 깊이 제한 또는 null일 때 호출 중단
			return CommentResponseDto.builder()
				.commentId(comment.getCommentId())
				.userName(comment.getArticle().getWriter().getUserName())
				.customId(comment.getArticle().getWriter().getCustomId())
				.content(comment.getContent())
				.deletedStatus(comment.getDeletedStatus())
				.viewCnt(comment.getViewCnt())
				.likeCnt(stats.getLikeCnt())
				.commentCnt(stats.getCommentCnt())
				.owner(isOwner)
				.comments(Collections.emptyList()) // 더 이상 하위 댓글 포함 안 함
				.build();
		}

		return CommentResponseDto.builder()
			.commentId(comment.getCommentId())
			.userName(comment.getArticle().getWriter().getUserName())
			.customId(comment.getArticle().getWriter().getCustomId())
			.content(comment.getContent())
			.deletedStatus(comment.getDeletedStatus())
			.viewCnt(comment.getViewCnt())
			.likeCnt(stats.getLikeCnt())
			.commentCnt(stats.getCommentCnt())
			.owner(isOwner)
			.comments(comment.getComments().stream()
				.filter(reply -> reply != null && reply.getDeletedStatus() == DeletedStatus.NOT_DELETED) // 삭제된 댓글 제외
				.map(reply -> {
					CommentStatsDto replyStats = replyStatsMap.getOrDefault(reply.getCommentId(), CommentStatsDto.from(0, 0));
					boolean isReplyOwner = reply.getWriter().getUserId().equals(comment.getWriter().getUserId());
					return CommentResponseDto.from(reply, isReplyOwner, replyStats, replyStatsMap, depth - 1); // 깊이 줄임
				})
				.collect(Collectors.toList()))
			.build();
	}
}

