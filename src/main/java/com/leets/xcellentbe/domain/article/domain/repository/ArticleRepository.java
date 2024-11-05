package com.leets.xcellentbe.domain.article.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leets.xcellentbe.domain.article.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

	List<Article> findByCreatedAtDesc(int size);

	List<Article> findByCursorCreatedAtDesc(Long cursor, int size);
}
