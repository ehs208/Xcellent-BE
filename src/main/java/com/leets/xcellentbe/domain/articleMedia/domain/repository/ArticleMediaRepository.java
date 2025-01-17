package com.leets.xcellentbe.domain.articleMedia.domain.repository;

import java.util.List;
import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;

@Repository
public interface ArticleMediaRepository extends JpaRepository<ArticleMedia, UUID> {

	List<ArticleMedia> findByArticle_ArticleId(UUID articleId);
}
