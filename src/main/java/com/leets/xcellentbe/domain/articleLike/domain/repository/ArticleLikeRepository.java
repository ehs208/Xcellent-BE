package com.leets.xcellentbe.domain.articleLike.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.articleLike.domain.ArticleLike;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, UUID> {
	Optional<ArticleLike> findByArticle_ArticleIdAndUser_UserId(UUID articleId, Long userId);
}
