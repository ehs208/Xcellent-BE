package com.leets.xcellentbe.domain.commentLike.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.leets.xcellentbe.domain.comment.domain.Comment;
import com.leets.xcellentbe.domain.commentLike.domain.CommentLike;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

public interface CommentLikeRepository extends JpaRepository<CommentLike, UUID> {

	Optional<CommentLike> findByComment_CommentIdAndUser_UserIdAndDeletedStatus(UUID commentId, Long userId, DeletedStatus status);

	@Query("SELECT COUNT(l) FROM CommentLike l WHERE l.comment = :comment AND l.deletedStatus = com.leets.xcellentbe.domain.shared.DeletedStatus.NOT_DELETED")
	long countLikesByComment(@Param("comment") Comment comment);
}
