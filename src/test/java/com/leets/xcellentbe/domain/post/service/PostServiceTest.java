package com.leets.xcellentbe.domain.post.service;

import static com.leets.xcellentbe.domain.shared.DeletedStatus.*;
import static java.lang.Boolean.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.leets.xcellentbe.domain.post.domain.Post;
import com.leets.xcellentbe.domain.post.domain.repository.PostRepository;
import com.leets.xcellentbe.domain.post.dto.ArticlesResponseDto;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;

@SpringBootTest
@Transactional
class PostServiceTest {

	@Autowired
	private PostService postService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Test
	@DisplayName("특정 사용자의 게시글 조회")
	void getArticles() {
		// given
		User user1 = User.create("qwer123456", "email", "userName", "password", "phoneNumber", 2024,
			12, 12);
		User user2 = User.create("qwer1234", "email", "userName", "password", "phoneNumber", 2024,
			12, 12);
		userRepository.saveAll(List.of(user1, user2));

		Post post1 = new Post(user1, "아 재미없다", NOT_DELETED, FALSE, null, null);
		Post post2 = new Post(user2, "그만할래", NOT_DELETED, FALSE, null, null);
		Post post3 = new Post(user2, "안녕하세요", NOT_DELETED, FALSE, null, null);
		postRepository.saveAll(List.of(post1, post2, post3));
		// when

		List<ArticlesResponseDto> articles = postService.getArticles("qwer1234");
		// then

		assertThat(articles).hasSize(2);
		assertThat(articles).extracting("content")
			.containsExactly("그만할래", "안녕하세요");
	}
}
