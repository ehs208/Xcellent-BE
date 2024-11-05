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

	// 게시글 조회 (미디어 필터 옵션 추가)
	public List<ArticlesResponseDto> getArticles(String customId, boolean mediaOnly) {
		User user = getUser(customId);
		List<Object[]> posts = getPosts(user);

		Map<Post, List<String>> groupedPosts = groupPostsByFilePath(posts, mediaOnly);

		return groupedPosts.entrySet().stream()
			.map(entry -> ArticlesResponseDto.of(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	// 게시글 파일 경로 그룹화 (미디어 필터링 조건 추가)
	private Map<Post, List<String>> groupPostsByFilePath(List<Object[]> posts, boolean mediaOnly) {
		return posts.stream()
			.filter(post -> !mediaOnly || post[1] != null) // 미디어 있는 경우만 필터링
			.collect(Collectors.groupingBy(
				post -> (Post)post[0],
				Collectors.mapping(post -> (String)post[1], Collectors.toList())
			));
	}

	// 유저 정보로 게시글 조회
	private List<Object[]> getPosts(User user) {
		return postRepository.findPostsByWriter(user);
	}

	// 유저 정보 조회
	private User getUser(String customId) {
		return userRepository.findByCustomId(customId).orElseThrow(UserNotFoundException::new);
	}
}
