package com.scholarship.service;

import com.scholarship.entity.Notification;
import com.scholarship.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * 發送審核結果通知給學生
     * 
     * @param studentId       學生 ID
     * @param reviewerId      審核人員 ID
     * @param result          審核結果 (APPROVED/REJECTED)
     * @param scholarshipName 獎學金名稱
     */
    @Transactional
    public Notification sendReviewNotification(Integer studentId, Integer reviewerId, String result,
            String scholarshipName) {
        Notification notification = new Notification();
        notification.setStudentId(studentId);
        notification.setReviewerId(reviewerId);
        notification.setIsRead(false);

        if ("APPROVED".equals(result)) {
            notification.setTitle("獎學金申請通過");
            notification.setContent("恭喜！您申請的「" + scholarshipName + "」獎學金已通過審核。");
        } else {
            notification.setTitle("獎學金申請未通過");
            notification.setContent("很抱歉，您申請的「" + scholarshipName + "」獎學金未通過審核。");
        }

        return notificationRepository.save(notification);
    }

    /**
     * 取得學生的所有通知
     * 
     * @param studentId 學生 ID
     * @return 通知列表
     */
    public List<Notification> getStudentNotifications(Integer studentId) {
        return notificationRepository.findByStudentIdOrderByNotificationIdDesc(studentId);
    }

    /**
     * 取得學生的未讀通知數量
     * 
     * @param studentId 學生 ID
     * @return 未讀數量
     */
    public long getUnreadCount(Integer studentId) {
        return notificationRepository.countByStudentIdAndIsReadFalse(studentId);
    }

    /**
     * 標記通知為已讀
     * 
     * @param notificationId 通知 ID
     * @param studentId      學生 ID (用於驗證所有權)
     */
    @Transactional
    public void markAsRead(Integer notificationId, Integer studentId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("找不到通知"));

        if (!studentId.equals(notification.getStudentId())) {
            throw new RuntimeException("無權限操作此通知");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    /**
     * 標記學生的所有通知為已讀
     * 
     * @param studentId 學生 ID
     */
    @Transactional
    public void markAllAsRead(Integer studentId) {
        List<Notification> unreadNotifications = notificationRepository.findByStudentIdAndIsReadFalse(studentId);
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }
}
