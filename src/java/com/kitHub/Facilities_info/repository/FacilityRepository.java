package com.kitHub.Facilities_info.repository;

import com.kitHub.Facilities_info.domain.facility.Facility;
import com.kitHub.Facilities_info.domain.facility.FacilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByNameContaining(String name);
    List<Facility> findByType(FacilityType type);
    List<Facility> findByNameContainingAndType(String name, FacilityType type);
    @Query("SELECT f FROM Facility f JOIN FETCH f.geoCoordinates")
    List<Facility> findAllWithGeoCoordinates();

    @Query("SELECT f FROM Facility f LEFT JOIN FETCH f.reviews WHERE f.id = :id")
    Optional<Facility> findByIdWithReviews(@Param("id") Long id);
}


