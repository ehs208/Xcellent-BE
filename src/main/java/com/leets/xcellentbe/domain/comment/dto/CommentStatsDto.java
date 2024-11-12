package com.leets.xcellentbe.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentStatsDto {
	private long likeCnt;
	private long commentCnt;

	@Builder
	private CommentStatsDto(long likeCnt, long commentCnt) {
		this.likeCnt = likeCnt;
		this.commentCnt = commentCnt;
	}

	public static CommentStatsDto from(long likeCnt, long commentCnt) {
		return CommentStatsDto.builder()
			.likeCnt(likeCnt)
			.commentCnt(commentCnt)
			.build();
	}
}
