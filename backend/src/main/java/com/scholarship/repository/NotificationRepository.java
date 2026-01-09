package com.scholarship.repository;

import com.scholarship.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    /**
     * 取得學生的所有通知，按 ID 降序排列（最新優先）
     */
    List<Notification> findByStudentIdOrderByNotificationIdDesc(Integer studentId);

    /**
     * 計算學生的未讀通知數量
     */
    long countByStudentIdAndIsReadFalse(Integer studentId);

    /**
     * 取得學生的未讀通知
     */
    List<Notification> findByStudentIdAndIsReadFalse(Integer studentId);
}
