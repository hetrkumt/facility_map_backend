package com.example.demo.repository.article;

import com.example.demo.domain.article.FAQ;
import com.example.demo.dto.article.ArticleTitleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {

    @Query("SELECT new com.example.demo.dto.article.ArticleTitleResponse(n.id, n.title) FROM Notice n ORDER BY n.createdAt DESC")
    List<ArticleTitleResponse> findAllNoticeSummariesOrderByCreatedAtDesc();

    Optional<FAQ> findById(Long id);
}
