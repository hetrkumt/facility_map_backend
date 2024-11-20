package com.example.demo.service;

import com.example.demo.domain.Image;
import com.example.demo.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final String uploadDir = "C:/uploaded_images/";

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image saveImage(MultipartFile file) throws IOException {
        // 디렉토리 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 저장
        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + fileName;
        file.transferTo(new File(filePath));

        // 엔티티 저장
        Image image = new Image();
        image.setFileName(fileName);
        image.setFilePath(filePath);
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setUploadTime(LocalDateTime.now());

        return imageRepository.save(image);
    }

    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }
}
