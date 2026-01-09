package com.scholarship.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 申請詳情 DTO - 用於審核人員查看申請完整信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDetailDTO {

    // 申請基本信息
    private Integer applicationId;
    private String status;
    private LocalDateTime submitDate;

    // 學生信息
    private Integer studentId;
    private String studentName;
    private String studentAccount;
    private String studentEmail;
    private String studentPhone;
    private String studentDepartment;
    private String studentGrade;

    // 獎學金信息
    private Integer scholarshipId;
    private String scholarshipName;
    private String scholarshipDescription;
    private BigDecimal scholarshipAmount;
    private String scholarshipType;
    private String scholarshipConditions;
    private LocalDateTime scholarshipDeadline;
    private LocalDateTime scholarshipStartDate;
    private Integer scholarshipQuota;

    // 推薦信信息
    private List<RecommendationDTO> recommendations;

    /**
     * 推薦信 DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendationDTO {
        private Integer advisorId;
        private String advisorName;
        private String content;
        private LocalDateTime fillDate;
        private boolean isSubmitted;  // 是否已提交
    }
}
