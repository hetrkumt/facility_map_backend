package com.kitHub.Facilities_info.repository.community;

import com.kitHub.Facilities_info.domain.community.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
