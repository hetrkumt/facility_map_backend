package com.example.demo.service;

import com.example.demo.domain.Facility;
import com.example.demo.domain.GeoCoordinates;
import com.example.demo.domain.User;
import com.example.demo.domain.UserReview;
import com.example.demo.dto.UpdateFacilityInfo;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.repository.GeoCoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityService {
    FacilityRepository facilityRepository;
    GeoCoordinatesRepository geoCoordinatesRepository;

    public Facility findById(Long facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected facility"));
    }

    public Facility updateUserReview (Facility facility, UserReview userReview) {
        facility.updateUserReview(userReview);
        return facilityRepository.save(facility);
    }

    public List<String> getFacilityNamesByNameContaining(String name) {
        List<Facility> facilities = facilityRepository.findByNameContaining(name);
        return facilities.stream()
                .map(Facility::getName)
                .collect(Collectors.toList());
    }
    @Autowired
    private ResourceLoader resourceLoader;

    public Facility saveFacility(UpdateFacilityInfo updateFacilityInfo) throws IOException {
        // 1. 이미지 파일 저장
        MultipartFile imageFile = updateFacilityInfo.getImage();
        String originalFileName = imageFile.getOriginalFilename();

        // 파일 이름 충돌 방지 (이미지 이름 + 시설 이름 + 현재 시간)
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = updateFacilityInfo.getName() + "_" + timestamp + "_" + originalFileName;

        // 2. 실제 서버의 파일 시스템 경로를 얻기 위해 ResourceLoader 사용
        Resource resource = resourceLoader.getResource("classpath:/static/images/");  // classpath 아래에 static/images/ 폴더
        Path uploadPath = Paths.get(resource.getURI()).toAbsolutePath();

        // 파일 저장 경로
        String imagePath = uploadPath.resolve(imageFileName).toString();  // 절대 경로

        // 파일을 지정된 폴더에 저장
        File directory = uploadPath.toFile();
        if (!directory.exists()) {
            directory.mkdirs();  // 디렉토리가 없으면 생성
        }

        // 이미지 파일을 저장
        imageFile.transferTo(new File(imagePath));

        // 3. Facility 엔티티 생성
        Facility facility = Facility.builder()
                .name(updateFacilityInfo.getName())
                .address(updateFacilityInfo.getAddress())
                .description(updateFacilityInfo.getDescription())
                .imageUrl("/images/" + imageFileName)  // 클라이언트가 접근할 수 있는 경로로 설정
                .build();

        // 4. Facility 엔티티 저장
        return facilityRepository.save(facility);
    }

    public Facility getFacilityByCoordinates(double latitude, double longitude) {
        // 위도와 경도로 GeoCoordinates 엔티티 찾기
        GeoCoordinates geoCoordinates = geoCoordinatesRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (geoCoordinates != null) {
            // GeoCoordinates를 바탕으로 매핑된 Facility 정보 찾기
            return geoCoordinates.getFacility();
        } else {
            return null;  // GeoCoordinates가 없으면 null 반환
        }
    }

}
