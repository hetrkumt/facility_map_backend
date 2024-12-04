package com.kitHub.Facilities_info.repository;

import com.kitHub.Facilities_info.domain.Report.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Block findByBlockedUserId(Long blockedUserId);
}
