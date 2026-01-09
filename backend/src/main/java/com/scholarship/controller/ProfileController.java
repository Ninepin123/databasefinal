package com.scholarship.controller;

import com.scholarship.dto.ChangePasswordRequest;
import com.scholarship.dto.ProfileDTO;
import com.scholarship.dto.UpdateProfileRequest;
import com.scholarship.security.JwtUtil;
import com.scholarship.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 個人資料控制器
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    /**
     * 取得個人資料
     */
    @GetMapping
    public ResponseEntity<ProfileDTO> getProfile(@RequestHeader("Authorization") String authHeader) {
        Integer userId = extractUserId(authHeader);
        ProfileDTO profile = profileService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    /**
     * 更新個人資料
     */
    @PutMapping
    public ResponseEntity<ProfileDTO> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UpdateProfileRequest request) {
        Integer userId = extractUserId(authHeader);
        ProfileDTO profile = profileService.updateProfile(userId, request);
        return ResponseEntity.ok(profile);
    }

    /**
     * 修改密碼
     */
    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ChangePasswordRequest request) {
        Integer userId = extractUserId(authHeader);
        profileService.changePassword(userId, request);
        return ResponseEntity.ok(Map.of("message", "密碼修改成功"));
    }

    /**
     * 從 Authorization header 取得 userId
     */
    private Integer extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("未提供有效的認證 Token");
        }
        String token = authHeader.substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }
}
