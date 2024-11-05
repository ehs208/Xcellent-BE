package com.leets.xcellentbe.domain.post.service;

import java.util.List;
import java.util.Map;
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

	// 전체 게시글 조회
	public List<ArticlesResponseDto> getArticles(String customId) {
		User user = getUser(customId);
		List<Object[]> posts = getPosts(user);

		Map<Post, List<String>> groupedPosts = groupPostsByFilePath(posts);

		return groupedPosts.entrySet().stream()
			.map(entry -> ArticlesResponseDto.from(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	// 미디어 있는게 게시글만 조회
	public List<ArticlesResponseDto> getArticlesWithMedia(String customId) {
		User user = getUser(customId);
		List<Object[]> posts = getPosts(user);

		Map<Post, List<String>> groupedPosts = groupPostsByFilePathWithMedia(posts);

		return groupedPosts.entrySet().stream()
			.map(entry -> ArticlesResponseDto.from(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	// 이미지만 있는 게시글 파일 경로 그룹화
	private Map<Post, List<String>> groupPostsByFilePathWithMedia(List<Object[]> posts) {
		return posts.stream()
			.filter(post -> post[1] != null)
			.collect(Collectors.groupingBy(
				post -> (Post)post[0],
				Collectors.mapping(post -> (String)post[1], Collectors.toList())
			));
	}

	// 전체 게시글 파일 경로 그룹화
	private Map<Post, List<String>> groupPostsByFilePath(List<Object[]> posts) {
		return posts.stream()
			.collect(Collectors.groupingBy(
				post -> (Post)post[0],
				Collectors.mapping(post -> (String)post[1], Collectors.toList())
			));
	}

	// 유저 정보로 게시글 조회
	private List<Object[]> getPosts(User user) {
		List<Object[]> posts = postRepository.findPostsByWriter(user);
		return posts;
	}

	// 유저 정보 조회
	private User getUser(String customId) {
		User user = userRepository.findByCustomId(customId).orElseThrow(UserNotFoundException::new);
		return user;
	}
}
