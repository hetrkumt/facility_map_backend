package com.kitHub.Facilities_info.service;

import com.kitHub.Facilities_info.domain.facility.GeoCoordinates;
import com.kitHub.Facilities_info.repository.GeoCoordinatesRepository;
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
