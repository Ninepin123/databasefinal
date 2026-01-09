package com.scholarship.controller;

import com.scholarship.entity.Advisor;
import com.scholarship.repository.AdvisorRepository;
import com.scholarship.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/advisors")
@CrossOrigin(origins = "*")
public class AdvisorController {

    @Autowired
    private AdvisorRepository advisorRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 取得所有老師列表（供學生選擇推薦信撰寫者）
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllAdvisors(
            @RequestHeader("Authorization") String authHeader) {
        // 驗證 token
        extractUserId(authHeader);

        List<Advisor> advisors = advisorRepository.findAllWithUser();

        // 轉換為前端需要的格式
        List<Map<String, Object>> result = advisors.stream()
                .map(advisor -> {
                    java.util.HashMap<String, Object> map = new java.util.HashMap<>();
                    map.put("advisorId", advisor.getUserId());
                    map.put("userId", advisor.getUserId());
                    map.put("name", advisor.getUser() != null ? advisor.getUser().getName() : "未知");
                    map.put("email", advisor.getUser() != null ? advisor.getUser().getEmail() : "");
                    map.put("department", advisor.getDepartment() != null ? advisor.getDepartment() : "");
                    map.put("title", advisor.getTitle() != null ? advisor.getTitle() : "");
                    map.put("office", advisor.getOffice() != null ? advisor.getOffice() : "");
                    return (Map<String, Object>) map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    private Integer extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }
        String token = authHeader.substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }
}
