package com.scholarship.service;

import com.scholarship.dto.ApplicationDetailDTO;
import com.scholarship.dto.ReviewRequestDTO;
import com.scholarship.entity.Application;
import com.scholarship.entity.Recommendation;
import com.scholarship.entity.ReviewRecord;
import com.scholarship.entity.Reviewer;
import com.scholarship.repository.ApplicationRepository;
import com.scholarship.repository.RecommendationRepository;
import com.scholarship.repository.ReviewRecordRepository;
import com.scholarship.repository.ReviewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewRecordService {

    @Autowired
    private ReviewRecordRepository reviewRecordRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ReviewerRepository reviewerRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * 提交審核（核心功能）
     * 
     * @param dto    審核請求
     * @param userId 審核人員的 userId
     * @return 創建的審核記錄
     */
    @Transactional
    public ReviewRecord createReview(ReviewRequestDTO dto, Integer userId) {
        // 1. 根據 userId 查詢審核人員
        Reviewer reviewer = reviewerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("找不到審核人員信息"));

        // 2. 查詢申請
        Application application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("找不到申請記錄"));

        // 3. 驗證申請狀態為 PENDING
        if (!"PENDING".equals(application.getStatus())) {
            throw new RuntimeException("此申請已經審核過，無法重複審核");
        }

        // 4. 檢查是否已經審核過
        if (reviewRecordRepository.existsByApplicationId(dto.getApplicationId())) {
            throw new RuntimeException("此申請已經審核過");
        }

        // 5. 創建審核記錄
        ReviewRecord reviewRecord = new ReviewRecord();
        reviewRecord.setReviewerId(reviewer.getUserId());
        reviewRecord.setApplicationId(dto.getApplicationId());
        reviewRecord.setResult(dto.getResult());
        reviewRecord.setReviewDate(LocalDateTime.now());

        reviewRecord = reviewRecordRepository.save(reviewRecord);

        // 6. 更新申請狀態
        application.setStatus(dto.getResult());
        applicationRepository.save(application);

        // 7. 發送通知給學生
        notificationService.sendReviewNotification(
                application.getStudentId(),
                reviewer.getUserId(),
                dto.getResult(),
                application.getScholarship().getName());

        return reviewRecord;
    }

    /**
     * 獲取待審核申請列表（含關聯信息）
     * 
     * @return 待審核申請列表
     */
    public List<Application> getPendingApplications() {
        return applicationRepository.findByStatusWithDetails("PENDING");
    }

    /**
     * 獲取申請完整詳情
     * 
     * @param applicationId 申請ID
     * @return 申請詳情 DTO
     */
    public ApplicationDetailDTO getApplicationDetail(Integer applicationId) {
        Application application = applicationRepository.findByIdWithDetails(applicationId);
        if (application == null) {
            throw new RuntimeException("找不到申請記錄");
        }

        ApplicationDetailDTO dto = new ApplicationDetailDTO();

        // 申請基本信息
        dto.setApplicationId(application.getApplicationId());
        dto.setStatus(application.getStatus());
        dto.setSubmitDate(application.getSubmitDate());

        // 學生信息
        dto.setStudentId(application.getStudent().getUserId());
        dto.setStudentName(application.getStudent().getUser().getName());
        dto.setStudentAccount(application.getStudent().getUser().getAccount());
        dto.setStudentEmail(application.getStudent().getUser().getEmail());
        dto.setStudentPhone(application.getStudent().getUser().getPhone());
        dto.setStudentDepartment(application.getStudent().getDepartment());
        dto.setStudentGrade(application.getStudent().getGrade());

        // 獎學金信息
        dto.setScholarshipId(application.getScholarship().getScholarshipId());
        dto.setScholarshipName(application.getScholarship().getName());
        dto.setScholarshipDescription(application.getScholarship().getDescription());
        dto.setScholarshipAmount(application.getScholarship().getAmount());
        dto.setScholarshipType(application.getScholarship().getType());
        dto.setScholarshipConditions(application.getScholarship().getConditions());
        dto.setScholarshipDeadline(application.getScholarship().getDeadline());
        dto.setScholarshipStartDate(application.getScholarship().getStartDate());
        dto.setScholarshipQuota(application.getScholarship().getQuota());

        // 推薦信信息
        List<Recommendation> recommendations = recommendationRepository.findById_ApplicationId(applicationId);
        List<ApplicationDetailDTO.RecommendationDTO> recommendationDTOs = recommendations.stream()
                .map(rec -> {
                    ApplicationDetailDTO.RecommendationDTO recDTO = new ApplicationDetailDTO.RecommendationDTO();
                    recDTO.setAdvisorId(rec.getId().getAdvisorId());
                    recDTO.setAdvisorName(rec.getAdvisor().getUser().getName());
                    recDTO.setContent(rec.getContent());
                    recDTO.setFillDate(rec.getFillDate());
                    recDTO.setSubmitted(rec.getContent() != null && !rec.getContent().isEmpty());
                    return recDTO;
                })
                .collect(Collectors.toList());
        dto.setRecommendations(recommendationDTOs);

        return dto;
    }

    /**
     * 獲取審核歷史記錄
     * 
     * @param userId 審核人員的 userId
     * @return 審核記錄列表
     */
    public List<ReviewRecord> getReviewHistory(Integer userId) {
        Reviewer reviewer = reviewerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("找不到審核人員信息"));

        return reviewRecordRepository.findByReviewerIdWithDetails(reviewer.getUserId());
    }
}
