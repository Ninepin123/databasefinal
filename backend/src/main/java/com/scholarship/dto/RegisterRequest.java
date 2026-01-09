package com.scholarship.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 註冊請求 DTO
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "帳號不能為空")
    @Size(min = 4, max = 50, message = "帳號長度需在 4-50 字元之間")
    private String account;

    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, max = 100, message = "密碼長度需在 6-100 字元之間")
    private String password;

    @NotBlank(message = "姓名不能為空")
    @Size(max = 50, message = "姓名最多 50 字元")
    private String name;

    @NotBlank(message = "電子郵件不能為空")
    @Email(message = "電子郵件格式不正確")
    private String email;

    private String phone;

    private String address;

    @NotBlank(message = "角色不能為空")
    private String role; // STUDENT, ADVISOR

    // 學生專用欄位
    private String department;
    private String grade;

    // 導師專用欄位
    private String office;
    private String title;
}
