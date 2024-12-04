package com.kitHub.Facilities_info.repository;

import com.kitHub.Facilities_info.domain.image.FacilityImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityImageRepository extends JpaRepository<FacilityImage, Long> {
}
