package com.kitHub.Facilities_info.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("upload/image")
    public String upload() {
        return "upload";
    }

    @GetMapping("/addNotice")
    public String addNotice() {
        return "addNotice";
    }

    @GetMapping("/addFAQ")
    public String addFAQ() {
        return "addFAQ";
    }

}
