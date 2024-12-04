package com.kitHub.Facilities_info.repository.article;

import com.kitHub.Facilities_info.domain.article.Notice;
import com.kitHub.Facilities_info.dto.article.ArticleTitleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findById(Long id);
}
