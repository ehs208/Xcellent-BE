package com.leets.xcellentbe.domain.commentLike.dto;

import java.util.UUID;

import com.leets.xcellentbe.domain.commentLike.domain.CommentLike;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentLikeResponseDto {
	private UUID commentId;
	private DeletedStatus status;

	@Builder
	private CommentLikeResponseDto(UUID commentId, DeletedStatus status) {
		this.commentId = commentId;
		this.status = status;
	}

	public static CommentLikeResponseDto from(CommentLike commentLike) {
		return CommentLikeResponseDto.builder()
			.commentId(commentLike.getComment().getCommentId())
			.status(commentLike.getDeletedStatus())
			.build();
	}
}
