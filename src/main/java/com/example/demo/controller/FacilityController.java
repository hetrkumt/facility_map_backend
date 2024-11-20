package com.example.demo.controller;

import com.example.demo.domain.Facility;

import com.example.demo.dto.UpdateFacilityInfo;
import com.example.demo.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/facilities/contain/{name}")
    public List<String> getFacilitiesByName(@PathVariable String name) {
        return facilityService.getFacilityNamesByNameContaining(name);
    }

    @PostMapping("update/facility/info")
    public ResponseEntity<Facility> updateFacility(@ModelAttribute UpdateFacilityInfo updateFacilityInfo) {
        try {
            // Facility 저장
            Facility savedFacility = facilityService.saveFacility(updateFacilityInfo);
            return ResponseEntity.ok(savedFacility);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/facility/info/by-coordinates")
    public ResponseEntity<MultiValueMap<String, Object>> getFacilityByCoordinates(@RequestParam double latitude, @RequestParam double longitude) {
        try {
            // 위도와 경도를 통해 Facility 찾기
            Facility facility = facilityService.getFacilityByCoordinates(latitude, longitude);

            if (facility != null) {
                // Facility 객체 정보와 이미지를 반환할 MultiValueMap 생성
                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

                // Facility 정보 (JSON 형식)
                body.add("facility", facility);

                // 이미지 경로
                String imageUrl = facility.getImageUrl();  // "/images/facility_20241120_123456.jpg"와 같은 형식

                // 클라이언트에서 접근할 수 있는 이미지 URL을 반환
                body.add("imageUrl", imageUrl);

                return ResponseEntity.ok().body(body);  // Facility 정보와 이미지 URL을 응답으로 반환
            } else {
                return ResponseEntity.notFound().build();  // Facility 정보가 없으면 404 반환
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);  // 예외 발생 시 500 오류 반환
        }
    }

    @GetMapping("facility/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            // 서버에 실제로 저장된 경로를 얻기 위한 절대 경로
            Resource resource = resourceLoader.getResource("file:/var/www/static/images/" + imageName);  // 서버의 절대 경로
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)  // 이미지의 콘텐츠 타입 (JPEG, PNG 등)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(404).build();  // 이미지가 없으면 404 반환
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();  // 예외 발생 시 500 오류 반환
        }
    }

}



