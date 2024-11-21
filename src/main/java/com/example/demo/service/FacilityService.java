package com.example.demo.service;

import com.example.demo.domain.Facility;
import com.example.demo.domain.GeoCoordinates;
import com.example.demo.domain.User;
import com.example.demo.domain.UserReview;
import com.example.demo.dto.UpdateFacilityInfo;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.repository.GeoCoordinatesRepository;
import jakarta.transaction.Transactional;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacilityService {
    @Autowired
    FacilityRepository facilityRepository;
    @Autowired
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


    @Transactional
    public Facility updateFacilityDetails(Long facilityId, UpdateFacilityInfo updateFacilityInfo) throws IOException {
        try {
            // 이미지 파일 저장 및 경로 얻기
            String imageFileName = saveImageFile(updateFacilityInfo.getImage(), updateFacilityInfo.getName());

            // Facility 찾기 및 업데이트
            Optional<Facility> foundFacility = facilityRepository.findById(facilityId);
            if (foundFacility.isPresent()) {
                Facility facility = foundFacility.get();

                facility.setName(updateFacilityInfo.getName());
                facility.setAddress(updateFacilityInfo.getAddress());
                facility.setDescription(updateFacilityInfo.getDescription());
                facility.setImageUrl("/images/" + imageFileName); // 이미지 URL 설정

                return facilityRepository.save(facility);
            } else {
                throw new IllegalArgumentException("Facility not found with id " + facilityId);
            }
        } catch (IllegalArgumentException e) {
            // 예외 발생 시 메시지 출력
            System.out.println("Invalid input: " + e.getMessage());
            throw e; // 잘못된 입력 예외 다시 던지기
        } catch (IOException e) {
            // 예외 발생 시 메시지 출력
            System.out.println("File processing error: " + e.getMessage());
            throw e; // 파일 처리 예외 다시 던지기
        } catch (Exception e) {
            // 예외 발생 시 메시지 출력
            System.out.println("Unexpected error occurred: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    private String saveImageFile(MultipartFile imageFile, String facilityName) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Uploaded image file is empty or null");
        }

        String originalFileName = imageFile.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("Original file name is empty");
        }

        // 파일 이름 충돌 방지 (시설 이름 + 현재 시간 + 이미지 이름) 으로 파일 이름 설정
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = facilityName + "_" + timestamp + "_" + originalFileName;

        // 파일 업로드 될 경로 지정
        String uploadDirectory = Paths.get("src/main/resources/static/images/").toAbsolutePath().toString();
        System.out.println("파일 업로드 경로: " + uploadDirectory);

        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();  // 디렉토리가 없으면 생성
        }

        // 이미지 파일을 저장할 경로
        String imagePath = uploadDirectory + File.separator + imageFileName;  // 절대 경로

        // 현재 파일 경로 출력
        System.out.println("파일 절대 경로: " + imagePath);

        // 이미지 파일을 저장
        imageFile.transferTo(new File(imagePath));

        return imageFileName;
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
