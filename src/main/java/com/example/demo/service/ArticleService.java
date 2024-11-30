package com.example.demo.service;

import com.example.demo.domain.image.FAQImage;
import com.example.demo.domain.article.Notice;
import com.example.demo.domain.article.FAQ;
import com.example.demo.domain.image.NoticeImage;
import com.example.demo.dto.article.AddArticleRequest;
import com.example.demo.dto.article.ArticleTitleResponse;
import com.example.demo.repository.article.FAQImageRepository;
import com.example.demo.repository.article.NoticeRepository;
import com.example.demo.repository.article.FAQRepository;
import com.example.demo.repository.article.NoticeImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private FAQRepository faqRepository;

    @Autowired
    private NoticeImageRepository noticeImageRepository;

    @Autowired
    private FAQImageRepository faqImageRepository;

    @Autowired
    private ImageService imageService;

    public List<ArticleTitleResponse> getAllNoticesTitle() {
        System.out.println("Fetching all notices titles");
        return noticeRepository.findAll().stream()
                .map(notice -> new ArticleTitleResponse(notice.getId(), notice.getTitle()))
                .collect(Collectors.toList());
    }

    public Optional<Notice> findNoticeById(Long id) {
        System.out.println("Fetching notice with id: " + id);
        return noticeRepository.findById(id);
    }

    public Notice createNotice(AddArticleRequest addArticleRequest) throws IOException {
        System.out.println("Creating new notice with title: " + addArticleRequest.getTitle());

        // 이미지 파일이 있을 때만 저장 및 경로 얻기
        List<String> imageFileNames = new ArrayList<>();
        if (addArticleRequest.getImages() != null && addArticleRequest.getImages().length > 0) {
            imageFileNames = imageService.saveArticleImageFiles(List.of(addArticleRequest.getImages()), "notices");
        }

        Set<NoticeImage> noticeImages = imageFileNames.stream()
                .map(fileName -> new NoticeImage(fileName, null))
                .collect(Collectors.toSet());

        StringBuilder htmlContent = new StringBuilder(addArticleRequest.getContent());

        Notice notice = Notice.builder()
                .title(addArticleRequest.getTitle())
                .content(htmlContent.toString())
                .author(addArticleRequest.getAuthor())
                .images(noticeImages)
                .createdAt(LocalDateTime.now())
                .build();

        noticeImages.forEach(image -> image.setNotice(notice));

        Notice savedNotice = noticeRepository.save(notice);
        noticeImageRepository.saveAll(noticeImages);

        System.out.println("Notice created with id: " + savedNotice.getId());
        return savedNotice;
    }

    public List<ArticleTitleResponse> getAllFAQsTitle() {
        System.out.println("Fetching all FAQs titles");
        return faqRepository.findAll().stream()
                .map(faq -> new ArticleTitleResponse(faq.getId(), faq.getTitle()))
                .collect(Collectors.toList());
    }

    public Optional<FAQ> findFAQById(Long id) {
        System.out.println("Fetching FAQ with id: " + id);
        return faqRepository.findById(id);
    }

    public FAQ createFAQ(AddArticleRequest addArticleRequest) throws IOException {
        System.out.println("Creating new FAQ with title: " + addArticleRequest.getTitle());

        // 이미지 파일이 있을 때만 저장 및 경로 얻기
        List<String> imageFileNames = new ArrayList<>();
        if (addArticleRequest.getImages() != null && addArticleRequest.getImages().length > 0) {
            imageFileNames = imageService.saveArticleImageFiles(List.of(addArticleRequest.getImages()), "FAQ");
        }

        Set<FAQImage> faqImages = imageFileNames.stream()
                .map(fileName -> new FAQImage(fileName, null))
                .collect(Collectors.toSet());

        StringBuilder htmlContent = new StringBuilder(addArticleRequest.getContent());

        FAQ faq = FAQ.builder()
                .title(addArticleRequest.getTitle())
                .content(htmlContent.toString())
                .author(addArticleRequest.getAuthor())
                .images(faqImages)
                .createdAt(LocalDateTime.now())
                .build();

        faqImages.forEach(image -> image.setFaq(faq));

        FAQ savedFAQ = faqRepository.save(faq);
        faqImageRepository.saveAll(faqImages);

        System.out.println("FAQ created with id: " + savedFAQ.getId());
        return savedFAQ;
    }
}

