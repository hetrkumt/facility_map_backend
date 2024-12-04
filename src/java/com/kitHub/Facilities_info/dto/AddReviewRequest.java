package com.kitHub.Facilities_info.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddReviewRequest {
    private Long facilityId;
    private String reviewComment;
    private Integer rating;
}
