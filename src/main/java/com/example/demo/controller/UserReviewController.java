package com.example.demo.controller;

import com.example.demo.domain.UserReview;
import com.example.demo.dto.UpdateReviewRequest;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserReviewRepository;
import com.example.demo.service.ReviewService;
import com.example.demo.dto.AddReviewRequest;
import com.example.demo.util.Authentication.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/review")
public class UserReviewController {

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserReviewRepository userReviewRepository;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody AddReviewRequest dto) {
        Long reviewId = reviewService.saveReview(dto);
        if (reviewId!= null) {
            return ResponseEntity.ok("리뷰가 정삭적으로 작성 되었습니다. " + reviewId);
        } else {
            return ResponseEntity.badRequest().body("리뷰가 정삭적으로 작성되지 않았습니다.");
        }
    }

    @DeleteMapping("delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok("리뷰가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }


    @PutMapping("update/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestBody UpdateReviewRequest dto) {
        try {
            UserReview updatedReview = reviewService.updateReview(reviewId, dto);
            return ResponseEntity.ok(updatedReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/{reviewId}/like")
    public ResponseEntity<String> toggleLike(@PathVariable Long reviewId, @RequestParam boolean like) {
        try {
            UserReview updatedReview = reviewService.toggleLike(reviewId, like);
            return ResponseEntity.ok("좋아요 수정." + updatedReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}

