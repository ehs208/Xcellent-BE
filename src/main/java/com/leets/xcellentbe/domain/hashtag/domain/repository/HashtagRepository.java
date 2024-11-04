package com.leets.xcellentbe.domain.hashtag.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

	Optional<Hashtag> findByName(String name);

	@Query("SELECT h FROM Hashtag h WHERE h NOT IN (SELECT h2 FROM Article a JOIN a.hashtags h2)")
	List<Hashtag> findUnusedHashtags();
}
