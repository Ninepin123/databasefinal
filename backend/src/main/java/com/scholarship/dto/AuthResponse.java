package com.scholarship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 認證響應 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Integer userId;
    private String account;
    private String name;
    private String role;
    private String message;
}
