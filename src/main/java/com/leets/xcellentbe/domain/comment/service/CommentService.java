package com.leets.xcellentbe.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.article.domain.repository.ArticleRepository;
import com.leets.xcellentbe.domain.article.exception.ArticleNotFoundException;
import com.leets.xcellentbe.domain.comment.dto.CommentStatsDto;
import com.leets.xcellentbe.domain.commentLike.domain.repository.CommentLikeRepository;
import com.leets.xcellentbe.global.error.exception.custom.DeleteForbiddenException;
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
	private final CommentLikeRepository commentLikeRepository;
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
			newComment.updateParentComment(parentComment);
			commentList.add(newComment);
			parentComment.addComment(commentList);
			commentRepository.save(newComment);
		}
		else {
			// 댓글 생성
			Comment newComment = Comment.createComment(writer, content, targetArticle);
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
			commentRepository.save(targetComment);
		}
	}

	//대댓글 삭제
	public void deleteReply(HttpServletRequest request, UUID commentId, UUID replyId) {
		User user = getUser(request);

		// 부모 댓글 조회 (commentId)
		Comment parentComment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);
		// 대댓글(replyId) 조회
		Comment replyComment = commentRepository.findById(replyId)
			.orElseThrow(CommentNotFoundException::new);

		if (!replyComment.getWriter().getUserId().equals(user.getUserId())) {
			throw new DeleteForbiddenException();
		}

		// 대댓글이 해당 부모 댓글의 하위에 있는지 확인
		if (!replyComment.getParentComment().equals(parentComment)) {
			throw new DeleteForbiddenException();
		}
		replyComment.deleteComment();
		commentRepository.save(replyComment);
	}

	//댓글 단건 조회
	public CommentResponseDto getComment(HttpServletRequest request, UUID commentId) {

		User user = getUser(request);

		Comment targetComment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);

		targetComment.updateViewCount();
		boolean isOwner = targetComment.getWriter().getUserId().equals(user.getUserId());

		long likeCount = commentLikeRepository.countLikesByComment(targetComment);
		long replyCount = commentRepository.countRepliesByComment(targetComment);
		CommentStatsDto stats = CommentStatsDto.from(likeCount, replyCount);

		List<Comment> replies = commentRepository.findAllByParentCommentAndNotDeleted(targetComment);
		Map<UUID, CommentStatsDto> replyStatsMap = replies.stream()
			.collect(Collectors.toMap(
				Comment::getCommentId,
				reply -> {
					long replyLikeCount = commentLikeRepository.countLikesByComment(reply);
					long replyReplyCount = commentRepository.countRepliesByComment(reply);
					return CommentStatsDto.from(replyLikeCount, replyReplyCount);
				}
			));

		return CommentResponseDto.from(targetComment, isOwner, stats, replyStatsMap, 2);
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
