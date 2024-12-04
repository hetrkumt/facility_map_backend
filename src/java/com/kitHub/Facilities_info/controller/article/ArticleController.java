package com.kitHub.Facilities_info.controller.article;

import com.kitHub.Facilities_info.domain.article.FAQ;
import com.kitHub.Facilities_info.domain.article.Notice;
import com.kitHub.Facilities_info.dto.article.AddArticleRequest;
import com.kitHub.Facilities_info.dto.article.ArticleTitleResponse;
import com.kitHub.Facilities_info.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping(value = "/notices", consumes = "multipart/form-data")
    public ResponseEntity<String> createNotice(@ModelAttribute AddArticleRequest addArticleRequest) throws IOException {
        // addArticleRequest의 내용을 출력합니다.
        System.out.println("AddArticleRequest received:");
        System.out.println("Title: " + addArticleRequest.getTitle());
        System.out.println("Content: " + addArticleRequest.getContent());
        System.out.println("Author: " + addArticleRequest.getAuthor());


        Notice savedNotice = articleService.createNotice(addArticleRequest);

        // 성공 메시지와 함께 상태 코드 200 반환
        return ResponseEntity.ok("FAQ created successfully with ID: " + savedNotice.getId());
    }

    @GetMapping("/notices")
    public List<ArticleTitleResponse> getAllNoticesTitle() {
        return articleService.getAllNoticesTitle();
    }

    @GetMapping("/notices/{id}")
    public Optional<Notice> getNoticeById(@PathVariable Long id) {
        return articleService.findNoticeById(id);
    }


    @PostMapping(value = "/faqs", consumes = "multipart/form-data")
    public ResponseEntity<String> createFAQ(@ModelAttribute AddArticleRequest addArticleRequest) throws IOException {
        // addArticleRequest의 내용을 출력합니다.
        System.out.println("AddArticleRequest received:");
        System.out.println("Title: " + addArticleRequest.getTitle());
        System.out.println("Content: " + addArticleRequest.getContent());
        System.out.println("Author: " + addArticleRequest.getAuthor());

        FAQ savedFAQ = articleService.createFAQ(addArticleRequest);

        // 성공 메시지와 함께 상태 코드 200 반환
        return ResponseEntity.ok("FAQ created successfully with ID: " + savedFAQ.getId());
    }


    @GetMapping("/faqs")
    public List<ArticleTitleResponse> getAllFAQsTitle() {
        return articleService.getAllFAQsTitle();
    }

    @GetMapping("/faqs/{id}")
    public Optional<FAQ> getFAQById(@PathVariable Long id) {
        return articleService.findFAQById(id);
    }
}

