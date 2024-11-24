package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("upload/image")
    public String upload() {
        return "upload";
    }

    @GetMapping("/image")
    public String image() {
        return "image";
    }

}
