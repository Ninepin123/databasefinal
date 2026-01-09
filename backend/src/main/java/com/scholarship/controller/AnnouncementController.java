package com.scholarship.controller;

import com.scholarship.entity.Announcement;
import com.scholarship.service.AnnouncementService;
import com.scholarship.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "*")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "false") Boolean all) {
        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(announcementService.searchAnnouncements(keyword));
        }
        if (all) {
            return ResponseEntity.ok(announcementService.getAllAnnouncements());
        }
        return ResponseEntity.ok(announcementService.getActiveAnnouncements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable Integer id) {
        return ResponseEntity.ok(announcementService.getAnnouncementById(id));
    }

    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Announcement announcement) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        Integer adminId = jwtUtil.getUserIdFromToken(token);
        return ResponseEntity.ok(announcementService.createAnnouncement(announcement, adminId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Announcement announcement) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(announcementService.updateAnnouncement(id, announcement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnnouncement(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok(Map.of("message", "Announcement deleted successfully"));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Announcement> toggleAnnouncementActive(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(announcementService.toggleActive(id));
    }
}
