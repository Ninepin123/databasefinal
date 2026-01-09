package com.scholarship.service;

import com.scholarship.dto.AuthResponse;
import com.scholarship.dto.LoginRequest;
import com.scholarship.dto.RegisterRequest;
import com.scholarship.entity.*;
import com.scholarship.repository.*;
import com.scholarship.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 認證服務
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AdvisorRepository advisorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 使用者登入
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(() -> new RuntimeException("帳號或密碼錯誤"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("帳號或密碼錯誤");
        }

        String role = determineUserRole(user);
        String token = jwtUtil.generateToken(user.getUserId(), user.getAccount(), role);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .account(user.getAccount())
                .name(user.getName())
                .role(role)
                .message("登入成功")
                .build();
    }

    /**
     * 使用者註冊
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 檢查帳號是否存在
        if (userRepository.existsByAccount(request.getAccount())) {
            throw new RuntimeException("帳號已被使用");
        }

        // 檢查信箱是否存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("電子郵件已被使用");
        }

        // 建立 User
        User user = new User();
        user.setAccount(request.getAccount());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());

        user = userRepository.save(user);

        String role = request.getRole().toUpperCase();

        // 根據角色建立對應記錄
        switch (role) {
            case "STUDENT":
                Student student = new Student();
                student.setUserId(user.getUserId());
                student.setDepartment(request.getDepartment());
                student.setGrade(request.getGrade());
                studentRepository.save(student);
                break;

            case "ADVISOR":
                Advisor advisor = new Advisor();
                advisor.setUserId(user.getUserId());
                advisor.setDepartment(request.getDepartment());
                advisor.setOffice(request.getOffice());
                advisor.setTitle(request.getTitle());
                advisorRepository.save(advisor);
                break;

            default:
                throw new RuntimeException("不支援的角色類型");
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getAccount(), role);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .account(user.getAccount())
                .name(user.getName())
                .role(role)
                .message("註冊成功")
                .build();
    }

    /**
     * 判斷使用者角色
     */
    private String determineUserRole(User user) {
        if (user.getAdmin() != null) {
            return "ADMIN";
        } else if (user.getReviewer() != null) {
            return "REVIEWER";
        } else if (user.getAdvisor() != null) {
            return "ADVISOR";
        } else if (user.getStudent() != null) {
            return "STUDENT";
        }
        return "USER";
    }
}
