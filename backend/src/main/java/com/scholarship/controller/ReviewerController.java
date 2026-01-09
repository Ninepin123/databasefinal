package com.scholarship.controller;

import com.scholarship.dto.ApplicationDetailDTO;
import com.scholarship.dto.ReviewRequestDTO;
import com.scholarship.entity.Application;
import com.scholarship.entity.ReviewRecord;
import com.scholarship.security.JwtUtil;
import com.scholarship.service.ReviewRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 審核人員控制器
 */
@RestController
@RequestMapping("/api/reviewer")
@CrossOrigin(origins = "*")
public class ReviewerController {

    @Autowired
    private ReviewRecordService reviewRecordService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 獲取待審核申請列表
     * GET /api/reviewer/applications/pending
     */
    @GetMapping("/applications/pending")
    public ResponseEntity<List<Application>> getPendingApplications(
            @RequestHeader("Authorization") String authHeader) {

        // 驗證角色
        validateReviewerRole(authHeader);

        List<Application> applications = reviewRecordService.getPendingApplications();
        return ResponseEntity.ok(applications);
    }

    /**
     * 獲取申請詳情
     * GET /api/reviewer/applications/{id}
     */
    @GetMapping("/applications/{id}")
    public ResponseEntity<ApplicationDetailDTO> getApplicationDetail(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {

        // 驗證角色
        validateReviewerRole(authHeader);

        ApplicationDetailDTO detail = reviewRecordService.getApplicationDetail(id);
        return ResponseEntity.ok(detail);
    }

    /**
     * 提交審核
     * POST /api/reviewer/reviews
     */
    @PostMapping("/reviews")
    public ResponseEntity<?> submitReview(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ReviewRequestDTO reviewRequest) {

        // 驗證角色
        Integer userId = validateReviewerRole(authHeader);

        try {
            ReviewRecord reviewRecord = reviewRecordService.createReview(reviewRequest, userId);
            return ResponseEntity.ok(Map.of(
                    "message", "審核成功",
                    "reviewId", reviewRecord.getReviewId(),
                    "result", reviewRecord.getResult()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 獲取審核歷史記錄
     * GET /api/reviewer/reviews/history
     */
    @GetMapping("/reviews/history")
    public ResponseEntity<List<ReviewRecord>> getReviewHistory(
            @RequestHeader("Authorization") String authHeader) {

        // 驗證角色並獲取 userId
        Integer userId = validateReviewerRole(authHeader);

        List<ReviewRecord> history = reviewRecordService.getReviewHistory(userId);
        return ResponseEntity.ok(history);
    }

    /**
     * 驗證審核人員角色並返回 userId
     * @param authHeader Authorization header
     * @return userId
     */
    private Integer validateReviewerRole(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("無效的令牌");
        }

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"REVIEWER".equals(role)) {
            throw new RuntimeException("權限不足：需要審核人員角色");
        }

        return jwtUtil.getUserIdFromToken(token);
    }
}
