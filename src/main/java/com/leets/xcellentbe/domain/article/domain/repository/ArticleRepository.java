package com.leets.xcellentbe.domain.article.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leets.xcellentbe.domain.article.domain.Article;
import com.leets.xcellentbe.domain.shared.DeletedStatus;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

	Optional<Article> findByWriterId(Long writerId);

	Optional<Article> findByWriterIdAndIsPinned(Long writerId, boolean isPinned);

	Optional<Article> findByDeleteStatus(DeletedStatus deletedStatus);

	Optional<Article> findByHashTag(String hashTag);

	Optional<Article> findByRePost(String rePost);

	//리스트(목록) 찾으려면 Page활용 / 커서 페이징 찾기
}
