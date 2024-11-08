package com.leets.xcellentbe.domain.article.service;

import static com.leets.xcellentbe.domain.shared.DeletedStatus.*;
import static java.lang.Boolean.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.article.domain.repository.ArticleRepository;
import com.leets.xcellentbe.domain.article.dto.ArticlesResponseDto;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;

@SpringBootTest
@Transactional
class ArticleServiceTest {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	@DisplayName("특정 사용자의 게시글 조회")
	void getArticles() {
		// given
		User user1 = User.create("qwer123456", "email", "userName", "password", "phoneNumber", 2024,
			12, 12);
		User user2 = User.create("qwer1234", "email", "userName", "password", "phoneNumber", 2024,
			12, 12);
		userRepository.saveAll(List.of(user1, user2));

		Article article1 = new Article(user1, "아 재미없다", NOT_DELETED, FALSE, null, null);
		Article article2 = new Article(user2, "그만할래", NOT_DELETED, FALSE, null, null);
		Article article3 = new Article(user2, "안녕하세요", NOT_DELETED, FALSE, null, null);
		articleRepository.saveAll(List.of(article1, article2, article3));
		// when

		List<ArticlesResponseDto> articles = articleService.getArticles("qwer1234");
		// then

		assertThat(articles).hasSize(2);
		assertThat(articles).extracting("content")
			.containsExactlyInAnyOrder("그만할래", "안녕하세요");
	}
}
