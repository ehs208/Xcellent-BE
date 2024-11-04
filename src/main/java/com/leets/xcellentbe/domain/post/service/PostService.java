package com.leets.xcellentbe.domain.post.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.xcellentbe.domain.post.domain.Post;
import com.leets.xcellentbe.domain.post.domain.repository.PostRepository;
import com.leets.xcellentbe.domain.post.dto.ArticlesResponseDto;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;

	public List<ArticlesResponseDto> getArticles(String customId) {
		User user = userRepository.findByCustomId(customId).orElseThrow(UserNotFoundException::new);
		List<Object[]> posts = postRepository.findPostsByWriter(user);

		return posts.stream()
			.map(post -> ArticlesResponseDto.from((Post)post[0], (String)post[1]))
			.collect(Collectors.toList());
	}

	public List<ArticlesResponseDto> getArticlesWithMedia(String customId) {
		User user = userRepository.findByCustomId(customId).orElseThrow(UserNotFoundException::new);
		List<Object[]> posts = postRepository.findPostsByWriter(user);
		
		return posts.stream()
			.filter(post -> post[1] != null)
			.map(post -> ArticlesResponseDto.from((Post)post[0], (String)post[1]))
			.collect(Collectors.toList());
	}
}

