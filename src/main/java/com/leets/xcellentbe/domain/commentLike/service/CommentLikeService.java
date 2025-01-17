package com.leets.xcellentbe.domain.commentLike.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.xcellentbe.domain.comment.domain.Comment;
import com.leets.xcellentbe.domain.comment.domain.repository.CommentRepository;
import com.leets.xcellentbe.domain.comment.exception.CommentNotFoundException;
import com.leets.xcellentbe.domain.commentLike.domain.CommentLike;
import com.leets.xcellentbe.domain.commentLike.domain.repository.CommentLikeRepository;
import com.leets.xcellentbe.domain.commentLike.dto.CommentLikeResponseDto;
import com.leets.xcellentbe.domain.commentLike.exception.CommentLikeNotFoundException;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;
import com.leets.xcellentbe.global.auth.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {
	private final CommentLikeRepository commentLikeRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final JwtService jwtService;

	public CommentLikeResponseDto likeComment(HttpServletRequest request, UUID commentId) {
		User user = getUser(request);
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFoundException::new);
		List<CommentLike> commentLikeList = new ArrayList<>();

		Optional<CommentLike> existingLike = commentLikeRepository.findByComment_CommentIdAndUser_UserIdAndDeletedStatus(
			commentId, user.getUserId(), DeletedStatus.NOT_DELETED);
		if (existingLike.isPresent()) {
			return CommentLikeResponseDto.from(existingLike.get());
		}

		CommentLike commentLike = CommentLike.create(comment, user);
		commentLikeList.add(commentLike);
		comment.addCommentLike(commentLikeList);
		return CommentLikeResponseDto.from(commentLikeRepository.save(commentLike));
	}

	public void unLikeComment(HttpServletRequest request, UUID commentId) {
		User user = getUser(request);
		CommentLike commentLike = commentLikeRepository.findByComment_CommentIdAndUser_UserIdAndDeletedStatus(
				commentId, user.getUserId(), DeletedStatus.NOT_DELETED)
			.orElseThrow(CommentLikeNotFoundException::new);
		commentLike.deleteCommentLike();
		commentLikeRepository.save(commentLike);
	}

	//JWT 토큰 기반 사용자 정보 반환 메소드
	private User getUser(HttpServletRequest request) {
		User user = jwtService.extractAccessToken(request)
			.filter(jwtService::isTokenValid)
			.flatMap(jwtService::extractEmail)
			.flatMap(userRepository::findByEmail)
			.orElseThrow(UserNotFoundException::new);

		return user;
	}
}
