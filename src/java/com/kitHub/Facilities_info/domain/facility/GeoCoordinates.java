package com.kitHub.Facilities_info.domain.facility;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kitHub.Facilities_info.domain.facility.Facility;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "geo_coordinates")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class GeoCoordinates {  // 클래스명 변경
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @OneToOne(mappedBy = "geoCoordinates")
    @JsonManagedReference
    private Facility facility;

    @Builder
    public GeoCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}

