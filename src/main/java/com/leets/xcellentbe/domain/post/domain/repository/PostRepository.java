package com.leets.xcellentbe.domain.post.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.leets.xcellentbe.domain.post.domain.Post;
import com.leets.xcellentbe.domain.user.domain.User;

public interface PostRepository extends JpaRepository<Post, UUID> {
	@Query("SELECT p, pm.filePath FROM Post p LEFT JOIN PostMedia pm ON p.postId = pm.post.postId WHERE p.writer = :user")
	List<Object[]> findPostsByWriter(User user);
}
