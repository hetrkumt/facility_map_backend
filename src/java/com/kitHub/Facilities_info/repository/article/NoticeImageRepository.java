package com.kitHub.Facilities_info.repository.article;

import com.kitHub.Facilities_info.domain.image.NoticeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeImageRepository extends JpaRepository<NoticeImage, Long> {
}
