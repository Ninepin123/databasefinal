package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 審查員實體 - 對應 reviewers 資料表
 */
@Entity
@Table(name = "reviewers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Reviewer {

    @Id
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "ReviewUnit", length = 100)
    private String reviewUnit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private User user;

    // 關聯到審查記錄
    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    private List<ReviewRecord> reviewRecords;

    // 關聯到發送的通知
    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    private List<Notification> notifications;
}
