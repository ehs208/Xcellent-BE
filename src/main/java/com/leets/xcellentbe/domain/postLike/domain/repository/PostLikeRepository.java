package com.leets.xcellentbe.domain.postLike.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.postLike.domain.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
}
