package com.example.demo.repository.article;

import com.example.demo.domain.image.FAQImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQImageRepository extends JpaRepository<FAQImage, Long> {
}
