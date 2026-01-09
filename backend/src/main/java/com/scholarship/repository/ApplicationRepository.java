package com.scholarship.repository;

import com.scholarship.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByStudentId(Integer studentId);

    List<Application> findByScholarshipId(Integer scholarshipId);

    void deleteByScholarshipId(Integer scholarshipId);

    // 根據狀態查詢申請
    List<Application> findByStatus(String status);

    // 根據狀態查詢申請，按提交日期升序（先提交的先審核）
    List<Application> findByStatusOrderBySubmitDateAsc(String status);

    // 查詢待審核申請，帶上獎學金、學生、用戶信息
    @Query("SELECT a FROM Application a " +
           "JOIN FETCH a.scholarship s " +
           "JOIN FETCH a.student st " +
           "JOIN FETCH st.user u " +
           "WHERE a.status = :status " +
           "ORDER BY a.submitDate ASC")
    List<Application> findByStatusWithDetails(@Param("status") String status);

    // 根據申請ID查詢申請，帶上所有相關信息（獎學金、學生）
    @Query("SELECT a FROM Application a " +
           "JOIN FETCH a.scholarship s " +
           "JOIN FETCH a.student st " +
           "JOIN FETCH st.user u " +
           "WHERE a.applicationId = :applicationId")
    Application findByIdWithDetails(@Param("applicationId") Integer applicationId);
}
