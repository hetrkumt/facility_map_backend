package com.kitHub.Facilities_info.service;

import com.kitHub.Facilities_info.domain.Report.Block;
import com.kitHub.Facilities_info.domain.Report.Report;
import com.kitHub.Facilities_info.domain.User;
import com.kitHub.Facilities_info.domain.UserReview;
import com.kitHub.Facilities_info.domain.community.Comment;
import com.kitHub.Facilities_info.domain.community.Post;
import com.kitHub.Facilities_info.dto.CreateReportRequest;
import com.kitHub.Facilities_info.repository.BlockRepository;
import com.kitHub.Facilities_info.repository.ReportRepository;
import com.kitHub.Facilities_info.repository.UserRepository;
import com.kitHub.Facilities_info.repository.UserReviewRepository;
import com.kitHub.Facilities_info.repository.community.CommentRepository;
import com.kitHub.Facilities_info.repository.community.PostRepository;
import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserReviewRepository userReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Transactional
    public Report createReport(String contentType, Long contentId, CreateReportRequest request) {
        System.out.println("Creating report for contentType: " + contentType + ", contentId: " + contentId);

        User reporter = authenticationProvider.getUserInfoFromSecurityContextHolder();
        if (reporter == null) {
            System.out.println("Reporter not found.");
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        System.out.println("Reporter: " + reporter.getEmail());

        User reportedUser = getReportedUser(contentType, contentId);
        System.out.println("Reported User: " + reportedUser.getEmail());

        if (reportRepository.findByReporterIdAndReportedContentId(reporter.getId(), contentId).size() > 0) {
            System.out.println("Content already reported.");
            throw new IllegalArgumentException("이미 신고한 콘텐츠입니다.");
        }

        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reason(request.getReason())
                .type(request.getType())
                .reportDate(LocalDateTime.now())
                .status("PENDING")
                .reportedContentId(contentId)
                .reportedContentType(contentType)
                .build();

        // 신고를 만든 사용자와 신고된 사용자에 신고 인스턴스 추가
        reporter.getReportsMade().add(report);
        reportedUser.getReportsReceived().add(report);

        // 각각의 사용자 엔티티를 업데이트
        userRepository.save(reporter);
        userRepository.save(reportedUser);

        reportRepository.save(report);
        System.out.println("Report saved with ID: " + report.getId());

        // 신고 6번 누적 시 자동 차단
        if (reportedUser.getReportsReceived().size() >= 6) {
            System.out.println("User has been reported 6 times. Blocking user.");
            Block block = Block.builder()
                    .blockedUser(reportedUser)
                    .blockDate(LocalDateTime.now())
                    .status("ACTIVE")
                    .build();

            blockRepository.save(block);
            reportedUser.setBlocked(true);
            userRepository.save(reportedUser);
        }

        return report;
    }

    private User getReportedUser(String contentType, Long contentId) {
        switch (contentType) {
            case "comment":
                Comment comment = commentRepository.findById(contentId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
                return comment.getUser();
            case "post":
                Post post = postRepository.findById(contentId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));
                return post.getUser();
            case "review":
                UserReview review = userReviewRepository.findById(contentId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));
                return review.getUser();
            default:
                throw new IllegalArgumentException("잘못된 콘텐츠 유형입니다.");
        }
    }

    public List<Report> getReportsByUser(Long userId) {
        System.out.println("Fetching reports for user ID: " + userId);
        return reportRepository.findByReportedUserId(userId);
    }

    public List<Report> getAllReports() {
        System.out.println("Fetching all reports.");
        return reportRepository.findAll();
    }
}
