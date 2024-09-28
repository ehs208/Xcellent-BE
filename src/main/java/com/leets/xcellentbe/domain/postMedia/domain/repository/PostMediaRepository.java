package com.leets.xcellentbe.domain.postMedia.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.postMedia.domain.PostMedia;

public interface PostMediaRepository extends JpaRepository<PostMedia, UUID> {
}
