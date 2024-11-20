package com.example.demo.service;

import com.example.demo.domain.UserReview;
import com.example.demo.repository.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class UserReviewService {

    private final UserReviewRepository userReviewRepository;

    public List<UserReview> getReviewsByFacilityId(Long facilityId) {
        return userReviewRepository.findByFacilityId(facilityId);
    }
}
