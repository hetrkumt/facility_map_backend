package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetFacilityInfo {
    private String name;
    private String address;
    private String description;
    private String imageUrl;
    private int rating;
}
