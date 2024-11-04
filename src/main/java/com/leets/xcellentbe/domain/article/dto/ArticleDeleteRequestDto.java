package com.leets.xcellentbe.domain.article.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleDeleteRequestDto {
	private UUID articleId;
}
