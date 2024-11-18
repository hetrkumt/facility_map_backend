package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateReviewRequest {
    private String reviewComment;
    private Integer rating;
}
