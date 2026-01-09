package com.scholarship.repository;

import com.scholarship.entity.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewerRepository extends JpaRepository<Reviewer, Integer> {

    // 根據 userId 查詢審核人員
    Optional<Reviewer> findByUserId(Integer userId);

    // 檢查是否存在該用戶的審核人員
    boolean existsByUserId(Integer userId);

    // 根據 userId 查詢並帶上用戶信息
    @Query("SELECT r FROM Reviewer r JOIN FETCH r.user WHERE r.user.userId = :userId")
    Optional<Reviewer> findByUserIdWithUser(@Param("userId") Integer userId);
}
