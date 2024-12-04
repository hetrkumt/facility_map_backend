package com.kitHub.Facilities_info.controller.facility;

import com.kitHub.Facilities_info.domain.facility.GeoCoordinates;
import com.kitHub.Facilities_info.service.GeoCoordinatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GeoCoordinatesController {

    private final GeoCoordinatesService geoCoordinatesService;

    @GetMapping("/geo-coordinates/within-radius")
    public List<GeoCoordinates> getGeoCoordinatesWithinRadius(@RequestParam double latitude,
                                                              @RequestParam double longitude,
                                                              @RequestParam double radius) {
        System.out.println("latitude:" + latitude + ",longitude:" + longitude + ", radius:" + radius);

        List<GeoCoordinates> results = geoCoordinatesService.getGeoCoordinatesWithinRadius(latitude, longitude, radius);

        // 결과 리스트 출력
        for (GeoCoordinates geo : results) {
            System.out.println(geo);
        }

        return results;
    }

}
