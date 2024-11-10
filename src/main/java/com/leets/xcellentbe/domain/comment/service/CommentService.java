package com.leets.xcellentbe.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.article.domain.repository.ArticleRepository;
import com.leets.xcellentbe.domain.article.exception.ArticleNotFoundException;
import com.leets.xcellentbe.domain.article.exception.DeleteForbiddenException;
import com.leets.xcellentbe.domain.comment.domain.Comment;
import com.leets.xcellentbe.domain.comment.domain.repository.CommentRepository;
import com.leets.xcellentbe.domain.comment.dto.CommentCreateRequestDto;
import com.leets.xcellentbe.domain.comment.dto.CommentResponseDto;
import com.leets.xcellentbe.domain.comment.exception.CommentNotFoundException;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;
import com.leets.xcellentbe.global.auth.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

	private final ArticleRepository articleRepository;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final CommentRepository commentRepository;

	//댓글 작성
	public void createComment(HttpServletRequest request,
		CommentCreateRequestDto commentCreateRequestDto, UUID articleId, UUID parentCommentId) {

		List<Comment> commentList = new ArrayList<>();
		User writer = getUser(request);
		Article targetArticle = articleRepository.findById(articleId)
			.orElseThrow(ArticleNotFoundException::new);
		String content = commentCreateRequestDto.getContent();

		if (parentCommentId != null) {
			// 부모 댓글이 있으면 대댓글로 생성
			Comment parentComment = commentRepository.findById(parentCommentId)
				.orElseThrow(CommentNotFoundException::new);
			Comment newComment = Comment.createComment(writer, content, targetArticle);
			parentComment.updateParentComment(parentComment);
			newComment.plusCommentCount();
			commentList.add(newComment);
			parentComment.addComment(commentList);
			commentRepository.save(newComment);
		}
		else {
			// 댓글 생성
			Comment newComment = Comment.createComment(writer, content, targetArticle);
			targetArticle.plusCommentCount();
			commentList.add(newComment);
			targetArticle.addComments(commentList);
			commentRepository.save(newComment);
		}
	}

	//댓글 삭제 (상태 변경)
	public void deleteComment(HttpServletRequest request, UUID commentId) {
		User user = getUser(request);

		Comment targetComment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);

		if(!(targetComment.getWriter().getUserId().equals(user.getUserId()))){
			throw new DeleteForbiddenException();
		}

		if (targetComment.getParentComment() == null) {
			targetComment.deleteComment();
			targetComment.getArticle().minusCommentCount();
		}
		else{
			targetComment.deleteComment();
			targetComment.minusCommentCount();
		}
	}

	//댓글 단건 조회
	public CommentResponseDto getComment(HttpServletRequest request, UUID commentId) {

		User user = getUser(request);

		Comment targetComment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);

		targetComment.updateViewCount();
		boolean isOwner = targetComment.getWriter().getUserId().equals(user.getUserId());

		return CommentResponseDto.from(targetComment, isOwner);
	}

	private User getUser(HttpServletRequest request) {
		User user = jwtService.extractAccessToken(request)
			.filter(jwtService::isTokenValid)
			.flatMap(jwtService::extractEmail)
			.flatMap(userRepository::findByEmail)
			.orElseThrow(UserNotFoundException::new);

		return user;
	}
}
