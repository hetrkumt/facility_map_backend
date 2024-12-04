package com.kitHub.Facilities_info.repository;

import com.kitHub.Facilities_info.domain.Report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByReportedUserId(Long reportedUserId);
    List<Report> findByReporterIdAndReportedContentId(Long reporterId, Long reportedContentId);
}
