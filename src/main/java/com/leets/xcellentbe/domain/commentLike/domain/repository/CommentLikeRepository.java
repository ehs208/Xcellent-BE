package com.leets.xcellentbe.domain.commentLike.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.commentLike.domain.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, UUID> {
	Optional<CommentLike> findByComment_CommentIdAndUser_UserId(UUID commentId, Long userId);
}
