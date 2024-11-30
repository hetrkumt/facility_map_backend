package com.example.demo.domain;

import com.example.demo.domain.image.FacilityImage;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

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
    @JsonBackReference
    private GeoCoordinates geoCoordinates;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserReview> reviews = new HashSet<>();

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<FacilityImage> facilityImages = new HashSet<>();

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType type;

    @Builder
    public Facility(String name, String address, String description, double rating, GeoCoordinates geoCoordinates, Set<UserReview> reviews, Set<FacilityImage> facilityImages, FacilityType type) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.rating = rating;
        this.geoCoordinates = geoCoordinates;
        this.reviews = reviews != null ? reviews : new HashSet<>();
        this.facilityImages = facilityImages != null ? facilityImages : new HashSet<>();
        this.type = type;
    }

    public Facility updateUserReview(UserReview userReview) {
        if (this.reviews == null) {
            this.reviews = new HashSet<>();
        }
        this.reviews.add(userReview);

        double totalRating = 0;
        for (UserReview review : reviews) {
            totalRating += review.getRating();
        }
        this.rating = totalRating / reviews.size();

        return this;
    }

    public Facility updateFacility(String name, String address, String description, Set<FacilityImage> facilityImages) {
        this.name = (name != null && !name.isEmpty()) ? name : this.name;
        this.address = (address != null && !address.isEmpty()) ? address : this.address;
        this.description = (description != null && !description.isEmpty()) ? description : this.description;
        if (facilityImages != null && !facilityImages.isEmpty()) {
            this.facilityImages.addAll(facilityImages);
        }
        return this;
    }

    public void addFacilityImages(FacilityImage facilityImage) {
        this.facilityImages.add(facilityImage);
        facilityImage.setFacility(this);
    }

    public void removeFacilityImage(FacilityImage facilityImage) {
        this.facilityImages.remove(facilityImage);
        facilityImage.setFacility(null);
    }
}
