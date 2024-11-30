package com.example.demo.repository;

import com.example.demo.domain.image.FacilityImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityImageRepository extends JpaRepository<FacilityImage, Long> {
}
