package com.leets.xcellentbe.domain.hashtag.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leets.xcellentbe.domain.hashtag.domain.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

}
