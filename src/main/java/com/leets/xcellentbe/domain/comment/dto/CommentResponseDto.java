package com.leets.xcellentbe.domain.comment.dto;

import java.util.List;
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
	private Long writerId;
	private String content;
	private DeletedStatus deletedStatus;
	private UUID rePostId;
	private int viewCnt;
	private int likeCnt;
	private int commentCnt;
	private boolean owner;
	private List<CommentResponseDto> comments;

	@Builder
	private CommentResponseDto(UUID commentId, Long writerId, String content, DeletedStatus deletedStatus,
		UUID rePostId, int viewCnt, int likeCnt, int commentCnt, boolean owner, List<CommentResponseDto> comments) {
		this.commentId = commentId;
		this.writerId = writerId;
		this.content = content;
		this.deletedStatus = deletedStatus;
		this.rePostId = rePostId;
		this.viewCnt = viewCnt;
		this.likeCnt = likeCnt;
		this.commentCnt = commentCnt;
		this.owner = owner;
		this.comments = comments;
	}

	public static CommentResponseDto from(Comment comment, boolean isOwner) {
		return CommentResponseDto.builder()
			.commentId(comment.getCommentId())
			.content(comment.getContent())
			.deletedStatus(comment.getDeletedStatus())
			.writerId(comment.getWriter().getUserId())
			.viewCnt(comment.getViewCnt())
			.likeCnt(comment.getLikeCnt())
			.commentCnt(comment.getCommentCnt())
			.comments(comment.getComments().stream()
				.map(comments -> CommentResponseDto.from(comments, comments.getWriter().getUserId().equals(comments.getWriter().getUserId())))
				.collect(Collectors.toList()))
			.owner(isOwner)
			.build();
	}
}
