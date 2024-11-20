package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UpdateFacilityInfo {
    private MultipartFile image;   // 업로드된 파일
    private String name;           // JSON 형식의 추가 데이터 (예: 이미지에 대한 설명)
    private String address;
    private String description;
}
