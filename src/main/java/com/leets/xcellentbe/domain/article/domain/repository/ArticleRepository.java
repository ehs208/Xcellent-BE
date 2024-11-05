package com.leets.xcellentbe.domain.article.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.leets.xcellentbe.domain.article.domain.Article;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

	//List<Article> findByOrderByCreatedAtDesc(int size);
	@Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
	List<Article> findRecentArticles(Pageable pageable);
	/*List<Article> findByCreatedAtBeforeOrderByCreatedAtDesc(LocalDateTime cursor, int size);*/
	@Query("SELECT a FROM Article a WHERE a.createdAt < :cursor ORDER BY a.createdAt DESC")
	List<Article> findRecentArticles(@Param("cursor") LocalDateTime cursor, Pageable pageable);
}
