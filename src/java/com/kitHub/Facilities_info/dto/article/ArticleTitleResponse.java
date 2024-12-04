package com.kitHub.Facilities_info.dto.article;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArticleTitleResponse {
    private Long id;
    private String title;

    public ArticleTitleResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
