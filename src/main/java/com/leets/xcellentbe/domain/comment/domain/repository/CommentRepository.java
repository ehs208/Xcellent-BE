package com.leets.xcellentbe.domain.comment.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

	@Query("SELECT c FROM Comment c WHERE c.parentComment = :parentComment AND c.deletedStatus = com.leets.xcellentbe.domain.shared.DeletedStatus.NOT_DELETED")
	List<Comment> findAllByParentCommentAndNotDeleted(@Param("parentComment") Comment parentComment);

	@Query("SELECT c FROM Comment c WHERE c.article = :article AND c.deletedStatus = com.leets.xcellentbe.domain.shared.DeletedStatus.NOT_DELETED")
	List<Comment> findAllByArticleAndNotDeleted(@Param("article") Article article);

	@Query("SELECT COUNT(c) FROM Comment c WHERE c.article = :article AND c.deletedStatus = com.leets.xcellentbe.domain.shared.DeletedStatus.NOT_DELETED")
	long countCommentsByArticle(@Param("article") Article article);

	@Query("SELECT COUNT(c) FROM Comment c WHERE c.parentComment = :comment AND c.deletedStatus = com.leets.xcellentbe.domain.shared.DeletedStatus.NOT_DELETED")
	long countRepliesByComment(@Param("comment") Comment comment);
}
