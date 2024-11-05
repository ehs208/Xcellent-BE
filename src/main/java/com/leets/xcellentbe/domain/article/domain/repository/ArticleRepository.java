package com.leets.xcellentbe.domain.article.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leets.xcellentbe.domain.article.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {
	//메인 페이지 모든 게시글 조회
	Page<Article> findAll(Pageable pageable);
}
