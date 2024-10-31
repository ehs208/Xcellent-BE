package com.leets.xcellentbe.domain.post.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.post.domain.Post;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;

public interface PostRepository extends JpaRepository<Post, UUID> {
	List<Post> findByWriterAndDeletedStatus(User writer, DeletedStatus deletedStatus);
}
