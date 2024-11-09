package com.leets.xcellentbe.domain.article.dto;

import com.leets.xcellentbe.domain.article.domain.Article;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticlesWithMediaDto {
	private Article article;
	private String filePath;
}
