package com.scholarship.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ApplicationID")
    private Integer applicationId;

    @Column(name = "StudentID", nullable = false)
    private Integer studentId;

    @Column(name = "ScholarshipID", nullable = false)
    private Integer scholarshipId;

    @Column(name = "Status", length = 20)
    private String status; // PENDING, APPROVED, REJECTED

    @Column(name = "SubmitDate")
    private LocalDateTime submitDate;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentID", insertable = false, updatable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ScholarshipID", insertable = false, updatable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Scholarship scholarship;

    // 關聯到審查記錄
    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY)
    private List<ReviewRecord> reviewRecords;
}
