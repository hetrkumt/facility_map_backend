package com.example.demo.controller;

import com.example.demo.domain.Facility;

import com.example.demo.dto.UpdateFacilityInfo;
import com.example.demo.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
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

    @PutMapping("update/facility/info")
    public ResponseEntity<?> updateFacility(@PathVariable Long facilityId, @ModelAttribute UpdateFacilityInfo updateFacilityInfo) {
        try {
            Facility savedFacility = facilityService.updateFacilityDetails(facilityId, updateFacilityInfo);
            return ResponseEntity.ok(savedFacility);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();  // 예외 스택 트레이스 출력
            return ResponseEntity.badRequest().body(e.getMessage()); // 잘못된 입력 시 400 응답
        } catch (IOException e) {
            e.printStackTrace();  // 예외 스택 트레이스 출력
            return ResponseEntity.status(500).body("File processing error occurred");
        } catch (Exception e) {
            e.printStackTrace();  // 예외 스택 트레이스 출력
            return ResponseEntity.status(500).body("An unexpected error occurred");
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

    @GetMapping("/facility/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            // 서버에 실제로 저장된 경로를 얻기 위한 절대 경로
            String uploadDirectory = Paths.get("src/main/resources/static/images/").toAbsolutePath().toString();
            System.out.println("파일 업로드 경로: " + uploadDirectory);

            String imagePath = uploadDirectory + "/" + imageName;
            System.out.println("파일 경로: " + imagePath);
            Resource resource = new UrlResource(Paths.get(imagePath).toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(404).build();  // 이미지가 없으면 404 반환
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();  // 예외 발생 시 500 오류 반환
        }
    }

}



