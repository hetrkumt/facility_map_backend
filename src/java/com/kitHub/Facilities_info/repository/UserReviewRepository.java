package com.kitHub.Facilities_info.repository;

import com.kitHub.Facilities_info.domain.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    List<UserReview> findByFacilityId(Long facilityId);
}
