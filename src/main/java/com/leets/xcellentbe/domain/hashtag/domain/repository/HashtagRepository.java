package com.leets.xcellentbe.domain.hashtag.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, UUID> {

	List<Hashtag> findByArticle_ArticleId(UUID articleId);
}
