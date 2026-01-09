package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NotificationID")
    private Integer notificationId;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "IsRead")
    private Boolean isRead = false;

    @Column(name = "Title", length = 255)
    private String title;

    @Column(name = "ReviewerID")
    private Integer reviewerId;

    @Column(name = "StudentID")
    private Integer studentId;

    // 關聯到 Reviewer (可選，因為可能為null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewerID", insertable = false, updatable = false)
    private Reviewer reviewer;

    // 關聯到 Student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentID", insertable = false, updatable = false)
    private Student student;

    // Transient fields
    @Transient
    private String reviewerName;

    @Transient
    private String studentName;
}
