package com.example.demo.repository;

import com.example.demo.domain.GeoCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface GeoCoordinatesRepository extends JpaRepository<GeoCoordinates, Long> {

    @Query("SELECT g FROM GeoCoordinates g WHERE " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(g.latitude)) * cos(radians(g.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(g.latitude)))) < :radius")
    List<GeoCoordinates> findGeoCoordinatesWithinRadius(@Param("latitude") double latitude,
                                                        @Param("longitude") double longitude,
                                                        @Param("radius") double radius);

    // 위도와 경도를 통해 GeoCoordinates를 찾는 메서드
    GeoCoordinates findByLatitudeAndLongitude(double latitude, double longitude);
}

