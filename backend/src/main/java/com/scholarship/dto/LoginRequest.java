package com.scholarship.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 登入請求 DTO
 */
@Data
public class LoginRequest {

    @NotBlank(message = "帳號不能為空")
    private String account;

    @NotBlank(message = "密碼不能為空")
    private String password;
}
