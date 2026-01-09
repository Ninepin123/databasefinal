package com.scholarship.service;

import com.scholarship.dto.ChangePasswordRequest;
import com.scholarship.dto.ProfileDTO;
import com.scholarship.dto.UpdateProfileRequest;
import com.scholarship.entity.*;
import com.scholarship.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 個人資料服務
 */
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AdvisorRepository advisorRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 取得使用者個人資料
     */
    public ProfileDTO getProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        ProfileDTO.ProfileDTOBuilder builder = ProfileDTO.builder()
                .userId(user.getUserId())
                .account(user.getAccount())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress());

        // 檢查是否為管理員
        adminRepository.findById(userId).ifPresent(admin -> {
            builder.role("ADMIN");
        });

        // 檢查是否為學生
        studentRepository.findById(userId).ifPresent(student -> {
            // 如果已經是管理員，角色已被設定，這裡可能需要決定優先級
            // 假設管理員身分優先，或者覆蓋（看需求）。這裡假設若已是 ADMIN 就不覆蓋，或是允許覆蓋？
            // 根據一般邏輯，specific role like STUDENT/TEACHER might be more relevant for display
            // if they have it,
            // but ADMIN usually overrides capabilities.
            // Let's assume hierarchy: ADMIN > TEACHER > STUDENT > USER
            // primarily based on the frontend logic: fetch profile -> show role.

            // Current simple logic: check all, last one wins or use explicit checks.
            // Better to check specific roles and set.

            // Refined logic:
            if (builder.build().getRole() == null) {
                builder.role("STUDENT")
                        .department(student.getDepartment())
                        .grade(student.getGrade())
                        .advisorId(student.getAdvisorId());

                // 取得導師姓名
                if (student.getAdvisorId() != null) {
                    userRepository.findById(student.getAdvisorId())
                            .ifPresent(advisor -> builder.advisorName(advisor.getName()));
                }
            }
        });

        // 檢查是否為導師
        advisorRepository.findById(userId).ifPresent(advisor -> {
            if (builder.build().getRole() == null) {
                builder.role("TEACHER") // Change ADVISOR to TEACHER for frontend
                        .department(advisor.getDepartment())
                        .office(advisor.getOffice())
                        .title(advisor.getTitle());
            }
        });

        // 如果沒有特定角色，設為一般使用者
        ProfileDTO profile = builder.build();
        if (profile.getRole() == null) {
            profile.setRole("USER");
        }

        return profile;
    }

    /**
     * 更新個人資料
     */
    @Transactional
    public ProfileDTO updateProfile(Integer userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        // 更新基本資料
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        userRepository.save(user);

        // 更新學生特定資料
        studentRepository.findById(userId).ifPresent(student -> {
            if (request.getDepartment() != null) {
                student.setDepartment(request.getDepartment());
            }
            if (request.getGrade() != null) {
                student.setGrade(request.getGrade());
            }
            studentRepository.save(student);
        });

        // 更新導師特定資料
        advisorRepository.findById(userId).ifPresent(advisor -> {
            if (request.getDepartment() != null) {
                advisor.setDepartment(request.getDepartment());
            }
            if (request.getOffice() != null) {
                advisor.setOffice(request.getOffice());
            }
            if (request.getTitle() != null) {
                advisor.setTitle(request.getTitle());
            }
            advisorRepository.save(advisor);
        });

        return getProfile(userId);
    }

    /**
     * 修改密碼
     */
    @Transactional
    public void changePassword(Integer userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        // 驗證舊密碼
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("舊密碼不正確");
        }

        // 更新密碼
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
