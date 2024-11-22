package com.leets.xcellentbe.domain.article.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.user.domain.User;

public interface ArticleRepository extends JpaRepository<Article, UUID> {

	@Query("SELECT DISTINCT a FROM Article a LEFT JOIN a.mediaList LEFT JOIN a.hashtags WHERE a.writer = :user")
	List<Article[]> findByWriter(User user);

	@Query("SELECT a FROM Article a WHERE a.deletedStatus = com.leets.xcellentbe.domain.shared.DeletedStatus.NOT_DELETED ORDER BY a.createdAt DESC")
	List<Article> findRecentArticles(Pageable pageable);

	@Query("SELECT a FROM Article a WHERE a.createdAt < :cursor AND a.deletedStatus = com.leets.xcellentbe.domain.shared.DeletedStatus.NOT_DELETED ORDER BY a.createdAt DESC")
	List<Article> findRecentArticles(@Param("cursor") LocalDateTime cursor, Pageable pageable);

	@Query("SELECT COUNT(a) FROM Article a WHERE a.rePost = :article AND a.deletedStatus = com.leets.xcellentbe.domain.shared.DeletedStatus.NOT_DELETED")
	long countReposts(@Param("article") Article article);
}
