package com.kitHub.Facilities_info.repository.article;

import com.kitHub.Facilities_info.domain.image.FAQImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQImageRepository extends JpaRepository<FAQImage, Long> {
}
