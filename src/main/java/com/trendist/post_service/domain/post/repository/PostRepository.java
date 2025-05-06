package com.trendist.post_service.domain.post.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.trendist.post_service.domain.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {
	Page<Post> findAllByDeletedFalse(Pageable pageable);

	Optional<Post> findByIdAndDeletedFalse(UUID postId);

	Page<Post> findByUserIdAndDeletedFalse(UUID userId, Pageable pageable);
}
