package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    @JsonBackReference
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(nullable = false, length = 1000)
    private String reviewComment;

    @Column(nullable = false)
    private LocalDateTime reviewDate;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private int dislikes;

    @Builder
    public UserReview(Facility facility, User user, String reviewComment, int rating, LocalDateTime reviewDate, int likes, int dislikes) {
        this.facility = facility;
        this.user = user;
        this.reviewComment = reviewComment;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public UserReview updateReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
        return this;
    }

    public UserReview updateRating(int rating) {
        this.rating = rating;
        return this;
    }

    public UserReview updateLikes(int likes) {
        this.likes = likes;
        return this;
    }

    public UserReview updateDislikes(int dislikes) {
        this.dislikes = dislikes;
        return this;
    }
}
