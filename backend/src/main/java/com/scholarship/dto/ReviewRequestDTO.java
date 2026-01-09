package com.scholarship.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 審核請求 DTO
 */
@Data
public class ReviewRequestDTO {

    @NotNull(message = "申請ID不能為空")
    private Integer applicationId;

    @NotBlank(message = "審核結果不能為空")
    @Pattern(regexp = "APPROVED|REJECTED", message = "審核結果只能是 APPROVED 或 REJECTED")
    private String result;
}
