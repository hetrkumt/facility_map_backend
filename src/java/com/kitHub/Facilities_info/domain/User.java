package com.kitHub.Facilities_info.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kitHub.Facilities_info.domain.Report.Block;
import com.kitHub.Facilities_info.domain.Report.Report;
import com.kitHub.Facilities_info.domain.community.Comment;
import com.kitHub.Facilities_info.domain.community.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = true, unique = true)
    private String snsId;

    @Column(nullable = false)
    private boolean isBlocked = false;  // 추가: 차단 여부

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<UserReview> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Comment> comments;

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "reporter")
    private Set<Report> reportsMade;  // 내가 신고한 기록

    @OneToMany(mappedBy = "reportedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "reportedUser")
    private Set<Report> reportsReceived;  // 나에 대한 신고 기록

    @OneToOne(mappedBy = "blockedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Block block;  // 추가: 차단 기록

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Builder
    public User(String email, String password, String nickname, String provider, String snsId, Set<UserReview> reviews, Set<Post> posts, Set<Comment> comments, Set<Report> reportsMade, Set<Report> reportsReceived, Block block) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.snsId = snsId;
        this.reviews = reviews;
        this.posts = posts;
        this.comments = comments;
        this.reportsMade = reportsMade;
        this.reportsReceived = reportsReceived;
        this.block = block;
    }

    public User updateNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public User updateUserReview(UserReview userReview) {
        if (this.reviews == null) {
            this.reviews = new HashSet<>();
        }
        this.reviews.add(userReview);
        return this;
    }

    public User updatePost(Post post) {
        this.posts = this.posts.stream()
                .map(p -> p.getId().equals(post.getId()) ? post : p)
                .collect(Collectors.toSet());
        return this;
    }

    public User updateComment(Comment comment) {
        this.comments = this.comments.stream()
                .map(c -> c.getId().equals(comment.getId()) ? comment : c)
                .collect(Collectors.toSet());
        return this;
    }

    public User addPost(Post post) {
        this.posts.add(post);
        post.setUser(this);
        return this;
    }

    public User addComment(Comment comment) {
        this.comments.add(comment);
        comment.setUser(this);
        return this;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBlocked;  // 수정: 차단 여부 반영
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isBlocked;  // 수정: 차단 여부 반영
    }
}
