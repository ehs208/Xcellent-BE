package com.leets.xcellentbe.domain.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleStatsDto {
	private long likeCnt;
	private long commentCnt;
	private long repostCnt;

	@Builder
	private ArticleStatsDto(long likeCnt, long commentCnt, long repostCnt) {
		this.likeCnt = likeCnt;
		this.commentCnt = commentCnt;
		this.repostCnt = repostCnt;
	}

	public static ArticleStatsDto from(long likeCnt, long commentCnt, long repostCnt) {
		return ArticleStatsDto.builder()
			.likeCnt(likeCnt)
			.commentCnt(commentCnt)
			.repostCnt(repostCnt)
			.build();
	}
}
