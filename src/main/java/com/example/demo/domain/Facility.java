package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "facilities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "geo_coordinates_id", referencedColumnName = "id")
    private GeoCoordinates geoCoordinates;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserReview> reviews;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String description;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private double rating;

    @Enumerated(EnumType.STRING) // FacilityType 열거형을 문자열로 저장
    @Column(nullable = false)
    private FacilityType type; // 시설 종류 필드 추가

    @Builder
    public Facility(String name, String address, String description, String imageUrl, double rating, GeoCoordinates geoCoordinates, Set<UserReview> reviews, FacilityType type) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.geoCoordinates = geoCoordinates;
        this.reviews = reviews != null ? reviews : new HashSet<>();
        this.type = type;
    }

    public Facility updateUserReview(UserReview userReview) {
        if (this.reviews == null) {
            this.reviews = new HashSet<>();
        }
        this.reviews.add(userReview);

        // 모든 리뷰의 점수를 합산하여 평균을 계산
        double totalRating = 0;
        for (UserReview review : reviews) {
            totalRating += review.getRating();
        }
        this.rating = totalRating / reviews.size();

        return this;
    }
}