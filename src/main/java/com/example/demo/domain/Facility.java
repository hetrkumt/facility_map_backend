package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
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
    private GeoCoordinates GeoCoordinates;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserReview> reviews;  // 일대다 관계 설정

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String description;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private int rating;

    @Builder
    public Facility(String name, String address, String description, String imageUrl, int rating, GeoCoordinates GeoCoordinates, Set<UserReview> reviews) {  // 매개변수 수정
        this.name = name;
        this.address = address;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.GeoCoordinates = GeoCoordinates;
        this.reviews = reviews;
    }

    public Facility updateUserReview(UserReview userReview) {
        if (this.reviews == null) {
            this.reviews = new HashSet<>();
        }
        this.reviews.add(userReview);
        return this;
    }
}
