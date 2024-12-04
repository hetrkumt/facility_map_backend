package com.kitHub.Facilities_info.controller;

import com.kitHub.Facilities_info.domain.Report.Block;
import com.kitHub.Facilities_info.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @DeleteMapping("/delete/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        try {
            adminService.deleteReview(reviewId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/delete/post/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        try {
            adminService.deletePost(postId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/delete/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        try {
            adminService.deleteComment(commentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/block/{userId}")
    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
        try {
            Block block = adminService.blockUser(userId);
            return ResponseEntity.ok("User blocked successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());  // 이미 블록된 상태 알림
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred.");
        }
    }

    @PostMapping("/unblock/{userId}")
    public ResponseEntity<Void> unblockUser(@PathVariable Long userId) {
        try {
            adminService.unblockUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/user-block/{userId}")
    public ResponseEntity<Block> getUserBlockRecord(@PathVariable Long userId) {
        try {
            Block block = adminService.getUserBlockRecord(userId);
            return ResponseEntity.ok(block);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/blocks")
    public ResponseEntity<List<Block>> getAllBlockRecords() {
        try {
            List<Block> blocks = adminService.getAllBlockRecords();
            return ResponseEntity.ok(blocks);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
