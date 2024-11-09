package com.leets.xcellentbe.domain.article.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.article.dto.ArticlesWithMediaDto;
import com.leets.xcellentbe.domain.user.domain.User;

import io.lettuce.core.dynamic.annotation.Param;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
	@Query("SELECT new com.leets.xcellentbe.domain.article.dto.ArticlesWithMediaDto(p, pm.filePath) FROM Article p LEFT JOIN PostMedia pm ON p.articleId = pm.article.articleId WHERE p.writer = :user")
	List<ArticlesWithMediaDto[]> findPostsByWriter(User user);

	@Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
	List<Article> findRecentArticles(Pageable pageable);

	@Query("SELECT a FROM Article a WHERE a.createdAt < :cursor ORDER BY a.createdAt DESC")
	List<Article> findRecentArticles(@Param("cursor") LocalDateTime cursor, Pageable pageable);

}
