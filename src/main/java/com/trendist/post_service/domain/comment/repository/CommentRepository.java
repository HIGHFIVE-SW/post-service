package com.trendist.post_service.domain.comment.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendist.post_service.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
