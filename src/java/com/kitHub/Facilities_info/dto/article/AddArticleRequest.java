package com.kitHub.Facilities_info.dto.article;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Setter
@Getter
public class AddArticleRequest {

    private String title;
    private String content;
    private String author;
    private MultipartFile[] images;

}
