package com.leets.xcellentbe.domain.article.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.article.dto.ArticlesWithMediaDto;
import com.leets.xcellentbe.domain.user.domain.User;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
	@Query("SELECT new com.leets.xcellentbe.domain.article.dto.ArticlesWithMediaDto(p, pm.filePath) FROM Article p LEFT JOIN PostMedia pm ON p.postId = pm.article.postId WHERE p.writer = :user")
	List<ArticlesWithMediaDto[]> findPostsByWriter(User user);
}
