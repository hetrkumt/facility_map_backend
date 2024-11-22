package com.example.demo.controller;

import com.example.demo.domain.GeoCoordinates;
import com.example.demo.service.GeoCoordinatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GeoCoordinatesController {

    private final GeoCoordinatesService geoCoordinatesService;

    @GetMapping("/geo-coordinates/within-radius")
    public List<GeoCoordinates> getGeoCoordinatesWithinRadius(@RequestParam double latitude,
                                                              @RequestParam double longitude,
                                                              @RequestParam double radius) {
        return geoCoordinatesService.getGeoCoordinatesWithinRadius(latitude, longitude, radius);
    }
}
