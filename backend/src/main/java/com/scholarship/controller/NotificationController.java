package com.scholarship.controller;

import com.scholarship.entity.Notification;
import com.scholarship.entity.Student;
import com.scholarship.repository.StudentRepository;
import com.scholarship.security.JwtUtil;
import com.scholarship.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 取得當前學生的通知列表
     * GET /api/notifications
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(
            @RequestHeader("Authorization") String authHeader) {

        Integer studentId = validateStudentAndGetId(authHeader);
        List<Notification> notifications = notificationService.getStudentNotifications(studentId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * 取得未讀通知數量
     * GET /api/notifications/unread-count
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @RequestHeader("Authorization") String authHeader) {

        Integer studentId = validateStudentAndGetId(authHeader);
        long count = notificationService.getUnreadCount(studentId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * 標記單則通知為已讀
     * PUT /api/notifications/{id}/read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {

        Integer studentId = validateStudentAndGetId(authHeader);
        try {
            notificationService.markAsRead(id, studentId);
            return ResponseEntity.ok(Map.of("message", "已標記為已讀"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 標記所有通知為已讀
     * PUT /api/notifications/read-all
     */
    @PutMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(
            @RequestHeader("Authorization") String authHeader) {

        Integer studentId = validateStudentAndGetId(authHeader);
        notificationService.markAllAsRead(studentId);
        return ResponseEntity.ok(Map.of("message", "所有通知已標記為已讀"));
    }

    /**
     * 驗證學生身份並取得學生 ID
     */
    private Integer validateStudentAndGetId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("無效的令牌");
        }

        String token = authHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);

        if (!"STUDENT".equals(role)) {
            throw new RuntimeException("權限不足：需要學生角色");
        }

        Integer userId = jwtUtil.getUserIdFromToken(token);
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("找不到學生資料"));

        return student.getUserId();
    }
}
