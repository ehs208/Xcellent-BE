package com.leets.xcellentbe.domain.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleStatsDto {
	private int likeCnt;
	private int commentCnt;
	private int repostCnt;

	@Builder
	private ArticleStatsDto(int likeCnt, int commentCnt, int repostCnt) {
		this.likeCnt = likeCnt;
		this.commentCnt = commentCnt;
		this.repostCnt = repostCnt;
	}

	public static ArticleStatsDto from(int likeCnt, int commentCnt, int repostCnt) {
		return ArticleStatsDto.builder()
			.likeCnt(likeCnt)
			.commentCnt(commentCnt)
			.repostCnt(repostCnt)
			.build();
	}
}
