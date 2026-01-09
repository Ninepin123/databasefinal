package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviewrecords")
@Data
@NoArgsConstructor
@AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ReviewRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewID")
    private Integer reviewId;

    @Column(name = "ReviewDate")
    private LocalDateTime reviewDate;

    @Column(name = "Result", length = 50)
    private String result; // "APPROVED" or "REJECTED"

    @Column(name = "ReviewerID")
    private Integer reviewerId;

    @Column(name = "ApplicationID")
    private Integer applicationId;

    // 關聯到 Application
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApplicationID", insertable = false, updatable = false)
    private Application application;

    // 關聯到 Reviewer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewerID", insertable = false, updatable = false)
    private Reviewer reviewer;

    // Transient fields for response DTOs
    @Transient
    private String reviewerName;

    @Transient
    private String scholarshipName;

    @Transient
    private String studentName;
}
