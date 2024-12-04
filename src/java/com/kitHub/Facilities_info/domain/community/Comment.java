package com.kitHub.Facilities_info.domain.community;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kitHub.Facilities_info.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference
    private Post post;

    @Builder
    public Comment(String content, User user, Post post, LocalDateTime modifiedAt) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.modifiedAt = modifiedAt;
    }

    public Comment updateContent(String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
        return this;
    }
}
