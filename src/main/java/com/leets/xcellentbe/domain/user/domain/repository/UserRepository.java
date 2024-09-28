package com.leets.xcellentbe.domain.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
