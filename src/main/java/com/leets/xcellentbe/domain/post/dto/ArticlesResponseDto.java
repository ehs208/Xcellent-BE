package com.leets.xcellentbe.domain.post.dto;

import java.util.List;

import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.post.domain.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ArticlesResponseDto {
	private String writer;
	private String content;
	private Boolean isPinned;
	private List<Hashtag> hashtags;
	private Post rePost;
	private List<String> filePath;

	@Builder
	private ArticlesResponseDto(String writer, String content, Boolean isPinned, List<Hashtag> hashtags, Post rePost,
		List<String> filePath) {
		this.writer = writer;
		this.content = content;
		this.isPinned = isPinned;
		this.hashtags = hashtags;
		this.rePost = rePost;
		this.filePath = filePath;
	}

	public static ArticlesResponseDto from(Post post, List<String> filePath) {
		return ArticlesResponseDto.builder()
			.writer(post.getWriter().getUserName())
			.content(post.getContent())
			.isPinned(post.getIsPinned())
			.hashtags(post.getHashtags())
			.rePost(post.getRePost())
			.filePath(filePath)
			.build();
	}
}
