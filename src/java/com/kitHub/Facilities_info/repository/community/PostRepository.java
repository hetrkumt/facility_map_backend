package com.kitHub.Facilities_info.repository.community;

import com.kitHub.Facilities_info.domain.community.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
