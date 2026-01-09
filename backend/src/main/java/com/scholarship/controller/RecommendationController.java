package com.scholarship.controller;

import com.scholarship.entity.Recommendation;
import com.scholarship.service.RecommendationService;
import com.scholarship.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/request")
    public ResponseEntity<?> requestRecommendation(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer applicationId = request.get("applicationId");
            Integer advisorId = request.get("advisorId");

            if (applicationId == null || advisorId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "applicationId 和 advisorId 為必填"));
            }

            recommendationService.requestRecommendation(applicationId, advisorId);
            return ResponseEntity.ok(Map.of("message", "推薦信請求已送出"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/advisor")
    public ResponseEntity<List<Map<String, Object>>> getAdvisorRequests(
            @RequestHeader("Authorization") String authHeader) {
        Integer userId = extractUserId(authHeader);
        List<Recommendation> recs = recommendationService.getRequestsByAdvisor(userId);

        // 轉換為包含學生資訊的 Map
        List<Map<String, Object>> result = recs.stream().map(rec -> {
            java.util.HashMap<String, Object> map = new java.util.HashMap<>();
            map.put("applicationId", rec.getId().getApplicationId());
            map.put("advisorId", rec.getId().getAdvisorId());
            map.put("content", rec.getContent());
            map.put("fillDate", rec.getFillDate());

            // Application 資訊
            if (rec.getApplication() != null) {
                java.util.HashMap<String, Object> appMap = new java.util.HashMap<>();
                appMap.put("applicationId", rec.getApplication().getApplicationId());
                appMap.put("scholarshipId", rec.getApplication().getScholarshipId());
                appMap.put("status", rec.getApplication().getStatus());
                appMap.put("submitDate", rec.getApplication().getSubmitDate());

                // Student 資訊
                if (rec.getApplication().getStudent() != null) {
                    var student = rec.getApplication().getStudent();
                    java.util.HashMap<String, Object> studentMap = new java.util.HashMap<>();
                    studentMap.put("userId", student.getUserId());
                    studentMap.put("department", student.getDepartment());
                    studentMap.put("grade", student.getGrade());
                    if (student.getUser() != null) {
                        studentMap.put("name", student.getUser().getName());
                        studentMap.put("email", student.getUser().getEmail());
                        studentMap.put("phone", student.getUser().getPhone());
                    }
                    appMap.put("student", studentMap);
                }

                // Scholarship 資訊
                if (rec.getApplication().getScholarship() != null) {
                    java.util.HashMap<String, Object> schMap = new java.util.HashMap<>();
                    schMap.put("scholarshipId", rec.getApplication().getScholarship().getScholarshipId());
                    schMap.put("name", rec.getApplication().getScholarship().getName());
                    appMap.put("scholarship", schMap);
                }

                map.put("application", appMap);
            }

            return (Map<String, Object>) map;
        }).collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/student")
    public ResponseEntity<List<Map<String, Object>>> getStudentRequests(
            @RequestHeader("Authorization") String authHeader) {
        Integer userId = extractUserId(authHeader);
        List<Recommendation> recs = recommendationService.getRequestsByStudent(userId);

        List<Map<String, Object>> result = recs.stream().map(rec -> {
            java.util.HashMap<String, Object> map = new java.util.HashMap<>();
            map.put("applicationId", rec.getId().getApplicationId());
            map.put("advisorId", rec.getId().getAdvisorId());
            map.put("content", rec.getContent());
            map.put("fillDate", rec.getFillDate());

            if (rec.getAdvisor() != null && rec.getAdvisor().getUser() != null) {
                map.put("advisorName", rec.getAdvisor().getUser().getName());
            } else {
                map.put("advisorName", "未知教授");
            }

            return (Map<String, Object>) map;
        }).collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitRecommendation(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> request) {
        Integer advisorId = extractUserId(authHeader);
        Integer applicationId = (Integer) request.get("applicationId");
        String content = (String) request.get("content");

        recommendationService.submitRecommendation(advisorId, applicationId, content);
        return ResponseEntity.ok(Map.of("message", "Recommendation submitted successfully"));
    }

    // ========== 新增 CRUD API 端點 ==========

    /**
     * 老師為學生建立推薦信
     */
    @PostMapping("/create")
    public ResponseEntity<?> createRecommendation(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> request) {
        try {
            Integer advisorId = extractUserId(authHeader);
            Integer applicationId = (Integer) request.get("applicationId");
            String content = (String) request.get("content");

            if (applicationId == null || content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "申請ID和內容為必填欄位"));
            }

            recommendationService.createRecommendation(advisorId, applicationId,
                    content);
            return ResponseEntity.ok(Map.of(
                    "message", "推薦信建立成功",
                    "applicationId", applicationId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 更新推薦信
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateRecommendation(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> request) {
        try {
            Integer advisorId = extractUserId(authHeader);
            Integer applicationId = (Integer) request.get("applicationId");
            String content = (String) request.get("content");

            if (applicationId == null || content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "申請ID和內容為必填欄位"));
            }

            recommendationService.updateRecommendation(advisorId, applicationId, content);
            return ResponseEntity.ok(Map.of("message", "推薦信更新成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 刪除推薦信
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRecommendation(@RequestHeader("Authorization") String authHeader,
            @RequestParam Integer applicationId) {
        try {
            Integer advisorId = extractUserId(authHeader);
            recommendationService.deleteRecommendation(advisorId, applicationId);
            return ResponseEntity.ok(Map.of("message", "推薦信刪除成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 取得特定學生的所有推薦信
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsByStudent(
            @PathVariable Integer studentId,
            @RequestHeader("Authorization") String authHeader) {
        // 驗證 token 有效性
        extractUserId(authHeader);
        List<Recommendation> recommendations = recommendationService.getRecommendationsByStudentId(studentId);
        return ResponseEntity.ok(recommendations);
    }

    private Integer extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }
        String token = authHeader.substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }
}
