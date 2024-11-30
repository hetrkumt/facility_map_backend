package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    public List<String> saveFacilityImageFiles(List<MultipartFile> imageFiles, String category) throws IOException {
        List<String> imageFileNames = new ArrayList<>();

        for (MultipartFile imageFile : imageFiles) {
            if (imageFile == null || imageFile.isEmpty()) {
                return imageFileNames;
            }

            String originalFileName = imageFile.getOriginalFilename();

            // 파일 이름 충돌 방지 (UUID + 현재 시간 + 이미지 이름) 으로 파일 이름 설정
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String uniqueID = UUID.randomUUID().toString();
            String imageFileName = uniqueID + "_" + timestamp + "_" + originalFileName;

            // 파일 업로드 될 경로 지정 (카테고리에 따라 다르게 설정)
            String uploadDirectory = Paths.get("src/main/resources/static/images/" + category).toAbsolutePath().toString();
            System.out.println("파일 업로드 경로: " + uploadDirectory);

            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();  // 디렉토리가 없으면 생성
            }

            // 이미지 파일을 저장할 경로
            String imagePath = uploadDirectory + File.separator + imageFileName;  // 절대 경로

            // 현재 파일 경로 출력
            System.out.println("파일 절대 경로: " + imagePath);

            // 이미지 파일을 저장
            imageFile.transferTo(new File(imagePath));

            imageFileNames.add(imageFileName);
        }

        return imageFileNames;
    }

    public List<String> saveArticleImageFiles(List<MultipartFile> imageFiles, String category) throws IOException {
        List<String> imageFileNames = new ArrayList<>();

        for (MultipartFile imageFile : imageFiles) {
            if (imageFile == null || imageFile.isEmpty()) {
                return imageFileNames;
            }

            String originalFileName = imageFile.getOriginalFilename();

            // 파일 업로드 될 경로 지정 (카테고리에 따라 다르게 설정)
            String uploadDirectory = Paths.get("src/main/resources/static/images/" + category).toAbsolutePath().toString();
            System.out.println("파일 업로드 경로: " + uploadDirectory);

            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();  // 디렉토리가 없으면 생성
            }

            // 이미지 파일을 저장할 경로
            String imagePath = uploadDirectory + File.separator + originalFileName;  // 절대 경로

            // 현재 파일 경로 출력
            System.out.println("파일 절대 경로: " + imagePath);

            // 이미지 파일을 저장
            imageFile.transferTo(new File(imagePath));

            imageFileNames.add(originalFileName);
        }

        return imageFileNames;
    }
}


