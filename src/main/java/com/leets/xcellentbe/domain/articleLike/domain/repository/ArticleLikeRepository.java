package com.leets.xcellentbe.domain.articleLike.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.leets.xcellentbe.domain.articleLike.domain.ArticleLike;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, UUID> {
	Optional<ArticleLike> findByArticle_ArticleIdAndUser_UserId(UUID articleId, Long userId);

	@Query("SELECT COUNT(l) FROM ArticleLike l WHERE l.article.articleId = :articleId AND l.deletedStatus = com.leets.xcellentbe.domain.shared.DeletedStatus.NOT_DELETED")
	long countLikesByArticleId(@Param("articleId") UUID articleId);

	Optional<ArticleLike> findByArticle_ArticleIdAndUser_UserIdAndDeletedStatus(UUID articleId, Long userId, DeletedStatus status);

	Boolean existsByArticle_ArticleIdAndUser_UserIdAndDeletedStatus(UUID articleId, Long userId, DeletedStatus status);
}
