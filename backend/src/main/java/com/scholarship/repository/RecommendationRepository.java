package com.scholarship.repository;

import com.scholarship.entity.Recommendation;
import com.scholarship.entity.RecommendationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, RecommendationId> {
    List<Recommendation> findById_AdvisorId(Integer advisorId);

    List<Recommendation> findByApplication_Student_UserId(Integer userId);

    List<Recommendation> findById_ApplicationId(Integer applicationId);

    @Query("SELECT r FROM Recommendation r " +
            "LEFT JOIN FETCH r.application a " +
            "LEFT JOIN FETCH a.student s " +
            "LEFT JOIN FETCH s.user " +
            "LEFT JOIN FETCH a.scholarship " +
            "WHERE r.id.advisorId = :advisorId")
    List<Recommendation> findByAdvisorIdWithDetails(@Param("advisorId") Integer advisorId);

    @Query("SELECT r FROM Recommendation r " +
            "LEFT JOIN FETCH r.advisor adv " +
            "LEFT JOIN FETCH adv.user " +
            "WHERE r.application.student.userId = :studentId")
    List<Recommendation> findByStudentIdWithDetails(@Param("studentId") Integer studentId);
}
