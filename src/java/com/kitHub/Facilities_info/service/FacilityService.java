package com.kitHub.Facilities_info.service;

import com.kitHub.Facilities_info.domain.facility.Facility;
import com.kitHub.Facilities_info.domain.UserReview;
import com.kitHub.Facilities_info.domain.facility.FacilityType;
import com.kitHub.Facilities_info.domain.facility.GeoCoordinates;
import com.kitHub.Facilities_info.domain.image.FacilityImage;

import com.kitHub.Facilities_info.repository.FacilityRepository;
import com.kitHub.Facilities_info.repository.GeoCoordinatesRepository;
import com.kitHub.Facilities_info.repository.FacilityImageRepository;

import com.kitHub.Facilities_info.dto.UpdateFacilityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private GeoCoordinatesRepository geoCoordinatesRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FacilityImageRepository facilityImageRepository;

    public Facility findById(Long facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected facility"));
    }

    public Facility updateUserReview(Facility facility, UserReview userReview) {
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
            // 이미지 파일이 있을 때만 저장 및 경로 얻기
            List<String> imageFileNames = new ArrayList<>();
            if (updateFacilityInfo.getImages() != null && !updateFacilityInfo.getImages().isEmpty()) {
                imageFileNames = imageService.saveFacilityImageFiles(updateFacilityInfo.getImages(), "facilities");
            }

            // Facility 찾기 및 업데이트
            Facility facility = facilityRepository.findById(facilityId)
                    .orElseThrow(() -> new IllegalArgumentException("Facility not found with id " + facilityId));

            Set<FacilityImage> facilityImages = imageFileNames.stream()
                    .map(imageFileName -> FacilityImage.builder()
                            .url(imageFileName)
                            .facility(facility)
                            .build())
                    .collect(Collectors.toSet());

            Facility updatedFacility = facility.updateFacility(
                    updateFacilityInfo.getName(),
                    updateFacilityInfo.getAddress(),
                    updateFacilityInfo.getDescription(),
                    facilityImages
            );

            facilityImageRepository.saveAll(facilityImages);
            return facilityRepository.save(updatedFacility);

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

    public List<Facility> findByNameContaining(String name) {
        return facilityRepository.findByNameContaining(name);
    }

    public List<Facility> findByType(FacilityType type) {
        return facilityRepository.findByType(type);
    }

    public List<Facility> findByNameContainingAndType(String name, FacilityType type) {
        return facilityRepository.findByNameContainingAndType(name, type);
    }

    public List<Facility> findAllWithGeoCoordinates() {
        return facilityRepository.findAllWithGeoCoordinates();
    }

    public Optional<Facility> findByIdWithReviews(Long id) {
        return facilityRepository.findByIdWithReviews(id);
    }
}
