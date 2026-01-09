package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 學生實體 - 對應 students 資料表
 */
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Student {

    @Id
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "Department", length = 50)
    private String department;

    @Column(name = "Grade", length = 10)
    private String grade;

    @Column(name = "AdvisorID")
    private Integer advisorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AdvisorID", insertable = false, updatable = false)
    private Advisor advisor;
}
