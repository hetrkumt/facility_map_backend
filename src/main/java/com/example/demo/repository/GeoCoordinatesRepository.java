package com.example.demo.repository;

import com.example.demo.domain.GeoCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoCoordinatesRepository extends JpaRepository<GeoCoordinates, Long> {
}
