package com.leets.xcellentbe.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Optional<User> findByCustomId(String customId);;
	Optional<User> findByRefreshToken(String refreshToken);
}
