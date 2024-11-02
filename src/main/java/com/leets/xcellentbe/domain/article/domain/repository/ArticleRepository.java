package com.leets.xcellentbe.domain.article.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.article.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
}
