package com.scholarship.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 使用者個人資料 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private Integer userId;
    private String account;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;

    // 學生專用欄位
    private String department;
    private String grade;
    private Integer advisorId;
    private String advisorName;

    // 導師專用欄位
    private String office;
    private String title;
}
