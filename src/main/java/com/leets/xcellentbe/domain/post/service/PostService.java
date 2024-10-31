package com.leets.xcellentbe.domain.post.service;

import static com.leets.xcellentbe.domain.shared.DeletedStatus.*;

import java.util.List;

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
		List<Post> post = postRepository.findByWriterAndDeletedStatus(user, NOT_DELETED);
		return post.stream().map(ArticlesResponseDto::from).toList();
	}
}
