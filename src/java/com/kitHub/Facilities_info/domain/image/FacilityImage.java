package com.kitHub.Facilities_info.domain.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kitHub.Facilities_info.domain.facility.Facility;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "facility_images")
public class FacilityImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    @JsonBackReference
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
