package com.leets.xcellentbe.domain.hashtag.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
