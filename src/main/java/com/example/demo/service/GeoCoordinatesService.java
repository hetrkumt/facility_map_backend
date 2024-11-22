package com.example.demo.service;

import com.example.demo.domain.GeoCoordinates;
import com.example.demo.repository.GeoCoordinatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeoCoordinatesService {

    private final GeoCoordinatesRepository geoCoordinatesRepository;

    public List<GeoCoordinates> getGeoCoordinatesWithinRadius(double latitude, double longitude, double radius) {
        return geoCoordinatesRepository.findGeoCoordinatesWithinRadius(latitude, longitude, radius);
    }
}
