package com.kitHub.Facilities_info.service;

import com.kitHub.Facilities_info.domain.facility.Facility;
import com.kitHub.Facilities_info.domain.User;
import com.kitHub.Facilities_info.domain.UserReview;
import com.kitHub.Facilities_info.dto.AddReviewRequest;
import com.kitHub.Facilities_info.dto.UpdateReviewRequest;
import com.kitHub.Facilities_info.repository.UserReviewRepository;
import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {

    @Autowired
    private UserReviewRepository userReviewRepository;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private UserService userService;

    @Transactional
    public Long saveReview(AddReviewRequest dto) {

        Long facilityId = dto.getFacilityId();
        String reviewComment = dto.getReviewComment();
        int rating = dto.getRating();

        User user = authenticationProvider.getUserInfoFromSecurityContextHolder();

        Facility facility = facilityService.findById(facilityId);

        UserReview savedReview = userReviewRepository.save(UserReview.builder()
                .facility(facility)
                .user(user)
                .reviewComment(reviewComment)
                .rating(rating)
                .reviewDate(LocalDateTime.now())
                .likes(0)
                .dislikes(0)
                .build());

        userService.updateUserReview(user,savedReview);
        facilityService.updateUserReview(facility,savedReview);

        return savedReview.getId();
    }
    @Transactional
    public void deleteReview(Long reviewId) {
        UserReview review = userReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));
        User user = authenticationProvider.getUserInfoFromSecurityContextHolder();
        if (!review.getUser().getId().equals(user.getId())) {
            throw new SecurityException("작성자만 글을 삭제 할 수 있습니다.");
        }

        userService.findById(review.getUser().getId()).getReviews().remove(review);
        facilityService.findById(review.getFacility().getId()).getReviews().remove(review);
        userReviewRepository.delete(review);
    }

    @Transactional
    public UserReview updateReview(Long reviewId, UpdateReviewRequest dto) {
        User user = authenticationProvider.getUserInfoFromSecurityContextHolder();
        UserReview review = userReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new SecurityException("작성자만 글을 수정할 수 있습니다.");
        }
        if (dto.getReviewComment() != null) {
            review.updateReviewComment(dto.getReviewComment());
        }
        if (dto.getRating() != null) {
            review.updateRating(dto.getRating());
        }
        return userReviewRepository.save(review);
    }

    @Transactional
    public UserReview toggleLike(Long reviewId, boolean like) {
        UserReview review = userReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));
        if (like) {
            review.updateLikes(review.getLikes() + 1);
        } else {
            review.updateLikes(review.getLikes() - 1);
        }
        return userReviewRepository.save(review);
    }
}




