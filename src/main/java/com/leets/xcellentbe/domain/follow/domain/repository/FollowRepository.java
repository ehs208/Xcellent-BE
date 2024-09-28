package com.leets.xcellentbe.domain.follow.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.follow.domain.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
