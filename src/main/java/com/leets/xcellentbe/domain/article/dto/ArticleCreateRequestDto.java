package com.leets.xcellentbe.domain.article.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleCreateRequestDto {
	private String content;
	private UUID rePostId;
	private Boolean isQuoteRepost;
}
