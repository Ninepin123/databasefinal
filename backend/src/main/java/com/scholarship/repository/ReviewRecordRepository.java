package com.scholarship.repository;

import com.scholarship.entity.ReviewRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRecordRepository extends JpaRepository<ReviewRecord, Integer> {

    // 根據審核人員ID查詢所有審核記錄
    List<ReviewRecord> findByReviewerId(Integer reviewerId);

    // 根據申請ID查詢審核記錄
    List<ReviewRecord> findByApplicationId(Integer applicationId);

    // 檢查某個申請是否已經被審核過
    boolean existsByApplicationId(Integer applicationId);

    // 根據審核人員ID查詢，按審核日期倒序排列
    List<ReviewRecord> findByReviewerIdOrderByReviewDateDesc(Integer reviewerId);

    // 查詢審核人員的特定結果的審核記錄
    List<ReviewRecord> findByReviewerIdAndResult(Integer reviewerId, String result);

    // 統計審核人員某個結果的數量
    long countByReviewerIdAndResult(Integer reviewerId, String result);

    // 查詢審核記錄並帶上申請、獎學金、學生信息（用於審核歷史）
    @Query("SELECT rr FROM ReviewRecord rr " +
           "JOIN FETCH rr.application a " +
           "JOIN FETCH a.scholarship s " +
           "JOIN FETCH a.student st " +
           "JOIN FETCH st.user u " +
           "WHERE rr.reviewerId = :reviewerId " +
           "ORDER BY rr.reviewDate DESC")
    List<ReviewRecord> findByReviewerIdWithDetails(@Param("reviewerId") Integer reviewerId);
}
