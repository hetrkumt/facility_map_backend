package com.kitHub.Facilities_info.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String showUploadPage() {
        return "testhtml"; // HTML 파일 이름 (확장자는 생략)
    }
}
