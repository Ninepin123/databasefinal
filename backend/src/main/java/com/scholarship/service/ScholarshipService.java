package com.scholarship.service;

import com.scholarship.entity.Scholarship;
import com.scholarship.repository.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScholarshipService {

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Autowired
    private com.scholarship.repository.ApplicationRepository applicationRepository;

    public List<Scholarship> getAllScholarships() {
        return scholarshipRepository.findAll();
    }

    public com.scholarship.entity.Scholarship getScholarshipById(Integer id) {
        return scholarshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship not found with id: " + id));
    }

    public List<Scholarship> searchScholarships(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllScholarships();
        }
        return scholarshipRepository.findByNameContainingIgnoreCase(keyword);
    }

    public void applyForScholarship(Integer studentId, Integer scholarshipId) {
        // Check if already applied
        List<com.scholarship.entity.Application> existing = applicationRepository.findByStudentId(studentId);
        boolean alreadyApplied = existing.stream()
                .anyMatch(a -> a.getScholarshipId().equals(scholarshipId));

        if (alreadyApplied) {
            throw new RuntimeException("You have already applied for this scholarship");
        }

        com.scholarship.entity.Application application = new com.scholarship.entity.Application();
        application.setStudentId(studentId);
        application.setScholarshipId(scholarshipId);
        application.setStatus("PENDING");
        application.setSubmitDate(java.time.LocalDateTime.now());

        applicationRepository.save(application);
    }

    public List<com.scholarship.entity.Application> getStudentApplications(Integer studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    public Scholarship createScholarship(Scholarship scholarship) {
        return scholarshipRepository.save(scholarship);
    }

    public Scholarship updateScholarship(Integer id, Scholarship scholarship) {
        Scholarship existing = scholarshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship not found with id: " + id));

        existing.setName(scholarship.getName());
        existing.setAmount(scholarship.getAmount());
        existing.setQuota(scholarship.getQuota());
        existing.setType(scholarship.getType());
        existing.setDeadline(scholarship.getDeadline());
        existing.setStartDate(scholarship.getStartDate());
        existing.setDescription(scholarship.getDescription());
        existing.setConditions(scholarship.getConditions());
        existing.setUnitId(scholarship.getUnitId());

        return scholarshipRepository.save(existing);
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteScholarship(Integer id) {
        if (!scholarshipRepository.existsById(id)) {
            throw new RuntimeException("Scholarship not found with id: " + id);
        }
        // 先刪除相關的申請記錄
        applicationRepository.deleteByScholarshipId(id);
        // 再刪除獎學金
        scholarshipRepository.deleteById(id);
    }

    /**
     * 學生刪除自己的申請（僅限待審核狀態）
     */
    @org.springframework.transaction.annotation.Transactional
    public void deleteStudentApplication(Integer studentId, Integer applicationId) {
        com.scholarship.entity.Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("找不到此申請"));

        // 確認是本人的申請
        if (!application.getStudentId().equals(studentId)) {
            throw new RuntimeException("您無權刪除此申請");
        }

        // 只能刪除待審核狀態的申請
        if (!"PENDING".equals(application.getStatus())) {
            throw new RuntimeException("只能刪除待審核狀態的申請");
        }

        applicationRepository.delete(application);
    }
}
