package com.example.demo.repository;

import com.example.demo.domain.GeoCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoCoordinatesRepository extends JpaRepository<GeoCoordinates, Long> {

    // 위도와 경도를 통해 GeoCoordinates를 찾는 메서드
    GeoCoordinates findByLatitudeAndLongitude(double latitude, double longitude);
}
