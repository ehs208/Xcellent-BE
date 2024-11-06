package com.leets.xcellentbe.domain.post.dto;

import com.leets.xcellentbe.domain.post.domain.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticlesWithMediaDto {
	private Post post;
	private String filePath;
}
