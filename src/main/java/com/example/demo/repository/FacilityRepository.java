package com.example.demo.repository;

import com.example.demo.domain.Facility;
import com.example.demo.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.Facility;
import com.example.demo.domain.FacilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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


