package com.example.demo.domain.image;

import com.example.demo.domain.Facility;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "images")
public class FacilityImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Builder
    public FacilityImage(String url, Facility facility) {
        this.url = url;
        this.facility = facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
