package com.leets.xcellentbe.domain.articleMedia.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.articleMedia.domain.ArticleMedia;

public interface ArticleMediaRepository extends JpaRepository<ArticleMedia, UUID> {
}
