package com.leets.xcellentbe.domain.article.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.article.domain.repository.ArticleRepository;
import com.leets.xcellentbe.domain.article.dto.ArticlesResponseDto;
import com.leets.xcellentbe.domain.article.dto.ArticlesWithMediaDto;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

	private final UserRepository userRepository;
	private final ArticleRepository articleRepository;

	// 게시글 조회 (미디어 필터 옵션 추가)
	public List<ArticlesResponseDto> getArticles(String customId, boolean mediaOnly) {
		User user = getUser(customId);
		List<ArticlesWithMediaDto[]> posts = getPosts(user);

		Map<Article, List<String>> groupedPosts = groupPostsByFilePath(posts, mediaOnly);

		return groupedPosts.entrySet().stream()
			.map(entry -> ArticlesResponseDto.of(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	// 게시글 파일 경로 그룹화 (미디어 필터링 조건 추가)
	private Map<Article, List<String>> groupPostsByFilePath(List<ArticlesWithMediaDto[]> posts, boolean mediaOnly) {
		return posts.stream()
			.flatMap(Arrays::stream)
			.filter(post -> !mediaOnly || post.getFilePath() != null) // 미디어 있는 경우만 필터링
			.collect(Collectors.groupingBy(
				ArticlesWithMediaDto::getArticle,
				Collectors.mapping(ArticlesWithMediaDto::getFilePath, Collectors.toList())
			));
	}

	// 유저 정보로 게시글 조회
	private List<ArticlesWithMediaDto[]> getPosts(User user) {
		return articleRepository.findPostsByWriter(user);
	}

	// 유저 정보 조회
	private User getUser(String customId) {
		return userRepository.findByCustomId(customId).orElseThrow(UserNotFoundException::new);
	}
}
