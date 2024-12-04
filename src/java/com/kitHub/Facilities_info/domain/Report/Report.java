package com.kitHub.Facilities_info.domain.Report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kitHub.Facilities_info.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    @JsonManagedReference
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    @JsonManagedReference
    private User reportedUser;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDateTime reportDate;

    @Column(nullable = false)
    private String status;

    @Column
    private String adminComments;

    @Column
    private Long reportedContentId;

    @Column
    private String reportedContentType;

    @ManyToOne
    @JoinColumn(name = "admin_reviewer_id")
    private User adminReviewer;

    @ManyToOne
    @JoinColumn(name = "block_id")
    @JsonBackReference
    private Block block;  // Block과 연관관계 추가

    @Builder
    public Report(User reporter, User reportedUser, String reason, String type, LocalDateTime reportDate, String status, String adminComments, Long reportedContentId, String reportedContentType, User adminReviewer, Block block) {
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.reason = reason;
        this.type = type;
        this.reportDate = reportDate;
        this.status = status;
        this.adminComments = adminComments;
        this.reportedContentId = reportedContentId;
        this.reportedContentType = reportedContentType;
        this.adminReviewer = adminReviewer;
        this.block = block;
    }
}
