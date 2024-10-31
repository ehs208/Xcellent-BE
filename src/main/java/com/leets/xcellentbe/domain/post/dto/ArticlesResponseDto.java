package com.leets.xcellentbe.domain.post.dto;

import java.util.List;

import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;
import com.leets.xcellentbe.domain.post.domain.Post;
import com.leets.xcellentbe.domain.user.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ArticlesResponseDto {
	private User writer;
	private String content;
	private Boolean isPinned;
	private List<Hashtag> hashtags;
	private Post rePost;

	@Builder
	public ArticlesResponseDto(Post post) {
		this.writer = post.getWriter();
		this.content = post.getContent();
		this.isPinned = post.getIsPinned();
		this.hashtags = post.getHashtags();
		this.rePost = post.getRePost();
	}

	public static List<ArticlesResponseDto> from(List<Post> post) {
		return post.stream().map(ArticlesResponseDto::new).toList();
	}
}
