package com.kitHub.Facilities_info.domain.image;

import com.kitHub.Facilities_info.domain.article.Notice;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "notice_images")
public class NoticeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "notice_id")
    @JsonBackReference
    private Notice notice;

    @Builder
    public NoticeImage(String url, Notice notice) {
        this.url = url;
        this.notice = notice;
    }
}
