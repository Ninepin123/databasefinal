package com.scholarship.controller;

import com.scholarship.entity.Student;
import com.scholarship.service.StudentService;
import com.scholarship.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 取得目前登入老師底下的所有學生
     */
    @GetMapping("/advisor")
    public ResponseEntity<List<Student>> getStudentsByAdvisor(@RequestHeader("Authorization") String authHeader) {
        Integer advisorId = extractUserId(authHeader);
        List<Student> students = studentService.getStudentsByAdvisor(advisorId);
        return ResponseEntity.ok(students);
    }

    /**
     * 取得單一學生詳細資料
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {
        Integer advisorId = extractUserId(authHeader);

        return studentService.getStudentById(id)
                .map(student -> {
                    // 檢查該學生是否屬於此導師
                    if (!advisorId.equals(student.getAdvisorId())) {
                        return ResponseEntity.status(403)
                                .body(Map.of("error", "您沒有權限查看此學生資料"));
                    }
                    return ResponseEntity.ok(student);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 取得所有學生（管理員用）
     */
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents(@RequestHeader("Authorization") String authHeader) {
        // TODO: 加入權限檢查，僅管理員可呼叫
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    private Integer extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }
        String token = authHeader.substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }
}
