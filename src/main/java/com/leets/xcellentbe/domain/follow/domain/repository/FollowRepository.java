package com.leets.xcellentbe.domain.follow.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.follow.domain.Follow;
import com.leets.xcellentbe.domain.user.domain.User;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	Optional<Follow> findByFollowerAndFollowing(User user, User targetUser);
	Page<Follow> findByFollower(User user, Pageable pageable);
	Page<Follow> findByFollowing(User user, Pageable pageable);
}
