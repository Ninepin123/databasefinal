package com.scholarship.service;

import com.scholarship.entity.Recommendation;
import com.scholarship.entity.RecommendationId;
import com.scholarship.entity.Advisor;
import com.scholarship.entity.Application;
import com.scholarship.repository.RecommendationRepository;
import com.scholarship.repository.ApplicationRepository;
import com.scholarship.repository.AdvisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AdvisorRepository advisorRepository;

    @Transactional
    public void requestRecommendation(Integer applicationId, Integer advisorId) {
        // Verify application exists
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Verify advisor exists
        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new RuntimeException("Advisor not found"));

        RecommendationId id = new RecommendationId(advisorId, applicationId);
        if (recommendationRepository.existsById(id)) {
            throw new RuntimeException("Recommendation already requested");
        }

        Recommendation recommendation = new Recommendation();
        recommendation.setId(id);
        recommendation.setAdvisor(advisor);
        recommendation.setApplication(application);
        recommendationRepository.save(recommendation);
    }

    public List<Recommendation> getRequestsByAdvisor(Integer advisorId) {
        return recommendationRepository.findByAdvisorIdWithDetails(advisorId);
    }

    public List<Recommendation> getRequestsByStudent(Integer studentId) {
        return recommendationRepository.findByStudentIdWithDetails(studentId);
    }

    @Transactional
    public void submitRecommendation(Integer advisorId, Integer applicationId, String content) {
        RecommendationId id = new RecommendationId(advisorId, applicationId);
        Recommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation request not found"));

        recommendation.setContent(content);
        recommendation.setFillDate(LocalDateTime.now());
        recommendationRepository.save(recommendation);
    }

    // ========== 新增 CRUD 方法 ==========

    /**
     * 老師直接為學生建立推薦信
     */
    @Transactional
    public Recommendation createRecommendation(Integer advisorId, Integer applicationId, String content) {
        // 驗證申請是否存在
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("申請資料不存在"));

        // 驗證導師是否存在
        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new RuntimeException("導師資料不存在"));

        RecommendationId id = new RecommendationId(advisorId, applicationId);

        // 檢查是否已存在
        if (recommendationRepository.existsById(id)) {
            throw new RuntimeException("該申請已有推薦信");
        }

        Recommendation recommendation = new Recommendation();
        recommendation.setId(id);
        recommendation.setContent(content);
        recommendation.setFillDate(LocalDateTime.now());
        recommendation.setAdvisor(advisor);
        recommendation.setApplication(application);

        return recommendationRepository.save(recommendation);
    }

    /**
     * 更新推薦信
     */
    @Transactional
    public Recommendation updateRecommendation(Integer advisorId, Integer applicationId, String content) {
        RecommendationId id = new RecommendationId(advisorId, applicationId);
        Recommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("推薦信不存在"));

        // 驗證是否為該導師的推薦信
        if (!recommendation.getId().getAdvisorId().equals(advisorId)) {
            throw new RuntimeException("您沒有權限修改此推薦信");
        }

        recommendation.setContent(content);
        recommendation.setFillDate(LocalDateTime.now());

        return recommendationRepository.save(recommendation);
    }

    /**
     * 刪除推薦信
     */
    @Transactional
    public void deleteRecommendation(Integer advisorId, Integer applicationId) {
        RecommendationId id = new RecommendationId(advisorId, applicationId);
        Recommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("推薦信不存在"));

        // 驗證是否為該導師的推薦信
        if (!recommendation.getId().getAdvisorId().equals(advisorId)) {
            throw new RuntimeException("您沒有權限刪除此推薦信");
        }

        recommendationRepository.delete(recommendation);
    }

    /**
     * 取得特定學生的所有推薦信
     */
    public List<Recommendation> getRecommendationsByStudentId(Integer studentId) {
        return recommendationRepository.findByApplication_Student_UserId(studentId);
    }

    /**
     * 取得單一推薦信
     */
    public Optional<Recommendation> getRecommendation(Integer advisorId, Integer applicationId) {
        RecommendationId id = new RecommendationId(advisorId, applicationId);
        return recommendationRepository.findById(id);
    }
}
