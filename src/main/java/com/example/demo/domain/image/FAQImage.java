package com.example.demo.domain.image;

import com.example.demo.domain.article.FAQ;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "faq_images")
public class FAQImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "faq_id")
    @JsonBackReference
    private FAQ faq;

    @Builder
    public FAQImage(String url, FAQ faq) {
        this.url = url;
        this.faq = faq;
    }

}
