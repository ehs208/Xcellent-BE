package com.leets.xcellentbe.domain.articleLike.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.articleLike.domain.ArticleLike;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, UUID> {
}
