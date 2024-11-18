package com.example.demo.service;

import com.example.demo.domain.Facility;
import com.example.demo.domain.User;
import com.example.demo.domain.UserReview;
import com.example.demo.repository.FacilityRepository;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {
    FacilityRepository facilityRepository;

    public Facility findById(Long facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected facility"));
    }

    public Facility updateUserReview (Facility facility, UserReview userReview) {
        facility.updateUserReview(userReview);
        return facilityRepository.save(facility);
    }
}
