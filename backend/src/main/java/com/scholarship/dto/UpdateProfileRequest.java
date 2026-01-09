package com.scholarship.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新個人資料請求 DTO
 */
@Data
public class UpdateProfileRequest {
    @NotBlank(message = "姓名不能為空")
    private String name;

    @Email(message = "電子郵件格式不正確")
    private String email;

    private String phone;
    private String address;

    // 學生專用
    private String department;
    private String grade;

    // 導師專用
    private String office;
    private String title;
}
