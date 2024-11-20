package com.example.demo.service;

import com.example.demo.domain.Facility;
import com.example.demo.domain.User;
import com.example.demo.domain.UserReview;
import com.example.demo.repository.FacilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getFacilityNamesByNameContaining(String name) {
        List<Facility> facilities = facilityRepository.findByNameContaining(name);
        return facilities.stream()
                .map(Facility::getName)
                .collect(Collectors.toList());
    }
}
