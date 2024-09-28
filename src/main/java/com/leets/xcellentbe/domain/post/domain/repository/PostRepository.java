package com.leets.xcellentbe.domain.post.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
