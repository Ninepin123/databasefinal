package com.scholarship.controller;

import com.scholarship.entity.Scholarship;
import com.scholarship.service.ScholarshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scholarships")
@CrossOrigin(origins = "*")
public class ScholarshipController {

    @Autowired
    private ScholarshipService scholarshipService;

    @Autowired
    private com.scholarship.security.JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Scholarship>> getAllScholarships(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(scholarshipService.searchScholarships(keyword));
        }
        return ResponseEntity.ok(scholarshipService.getAllScholarships());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scholarship> getScholarshipById(
            @org.springframework.web.bind.annotation.PathVariable Integer id) {
        return ResponseEntity.ok(scholarshipService.getScholarshipById(id));
    }

    @org.springframework.web.bind.annotation.PostMapping("/apply")
    public ResponseEntity<?> applyForScholarship(
            @org.springframework.web.bind.annotation.RequestHeader("Authorization") String authHeader,
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, Integer> request) {

        Integer userId = extractUserId(authHeader);
        Integer scholarshipId = request.get("scholarshipId");

        scholarshipService.applyForScholarship(userId, scholarshipId);
        return ResponseEntity.ok(java.util.Map.of("message", "Application submitted successfully"));
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<com.scholarship.entity.Application>> getMyApplications(
            @org.springframework.web.bind.annotation.RequestHeader("Authorization") String authHeader) {
        Integer userId = extractUserId(authHeader);
        return ResponseEntity.ok(scholarshipService.getStudentApplications(userId));
    }

    private Integer extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }
        String token = authHeader.substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }

    @org.springframework.web.bind.annotation.PostMapping
    public ResponseEntity<Scholarship> createScholarship(
            @org.springframework.web.bind.annotation.RequestHeader("Authorization") String authHeader,
            @org.springframework.web.bind.annotation.RequestBody Scholarship scholarship) {

        // Basic Role Check (This should ideally be done via Spring Security, but doing
        // manual check for now as per project style)
        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            // throw new RuntimeException("Unauthorized: Admin access required");
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(scholarshipService.createScholarship(scholarship));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<Scholarship> updateScholarship(
            @org.springframework.web.bind.annotation.PathVariable Integer id,
            @org.springframework.web.bind.annotation.RequestHeader("Authorization") String authHeader,
            @org.springframework.web.bind.annotation.RequestBody Scholarship scholarship) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(scholarshipService.updateScholarship(id, scholarship));
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScholarship(
            @org.springframework.web.bind.annotation.PathVariable Integer id,
            @org.springframework.web.bind.annotation.RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        scholarshipService.deleteScholarship(id);
        return ResponseEntity.ok(java.util.Map.of("message", "Scholarship deleted successfully"));
    }

    /**
     * 學生刪除自己的申請
     */
    @org.springframework.web.bind.annotation.DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<?> deleteApplication(
            @org.springframework.web.bind.annotation.PathVariable Integer applicationId,
            @org.springframework.web.bind.annotation.RequestHeader("Authorization") String authHeader) {
        try {
            Integer userId = extractUserId(authHeader);
            scholarshipService.deleteStudentApplication(userId, applicationId);
            return ResponseEntity.ok(java.util.Map.of("message", "申請已刪除"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
