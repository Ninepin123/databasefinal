package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 管理員實體 - 對應 admins 資料表
 */
@Entity
@Table(name = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "LastLoginTime")
    private LocalDateTime lastLoginTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private User user;
}
